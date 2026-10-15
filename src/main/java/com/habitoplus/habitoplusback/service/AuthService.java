package com.habitoplus.habitoplusback.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.habitoplus.habitoplusback.dto.AuthResponseDTO;
import com.habitoplus.habitoplusback.dto.ForgotPasswordRequestDTO;
import com.habitoplus.habitoplusback.dto.LoginRequestDTO;
import com.habitoplus.habitoplusback.dto.RegisterRequestDTO;
import com.habitoplus.habitoplusback.enums.RoleAccounts;
import com.habitoplus.habitoplusback.exception.InvalidCredentialsException;
import com.habitoplus.habitoplusback.exception.PixelaAccountCreationException;
import com.habitoplus.habitoplusback.exception.UserAlreadyExistsException;
import com.habitoplus.habitoplusback.model.Account;
import com.habitoplus.habitoplusback.model.Photo;
import com.habitoplus.habitoplusback.model.Pixela;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.model.Streak;
import com.habitoplus.habitoplusback.repository.AccountRepository;
import com.habitoplus.habitoplusback.repository.PixelaRepository;
import com.habitoplus.habitoplusback.repository.ProfileRepository;
import com.habitoplus.habitoplusback.repository.StreakRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;

    private final JsonWebTokenService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final StreakRepository streakRepository;

    private final PixelaRepository pixelaRepository;

    private final PixelaService pixelaService;
    
    private final ProfileRepository profileRepository;

    @Autowired
    private EmailService emailService;

    public AuthResponseDTO login(LoginRequestDTO request) {
        if (!accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new InvalidCredentialsException("Invalid email");
        }else if (!accountRepository.findByEmail(request.getEmail()).get().getStatus()) {
            throw new InvalidCredentialsException("Account is disabled");
        }
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Account user = accountRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.getToken(user);

        return AuthResponseDTO.builder().token(token).build();
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDate.now().format(dateFormatter);
        String formattedLastConnection = LocalDateTime.now().format(dateTimeFormatter);
        boolean isNotMinor = true;
        String thanksCode = "";
        // Creación de la cuenta Pixela
        String pixelaToken = generatePixelaToken();
        boolean pixelaCreated = pixelaService.createPixelaAccount(request.getEmail(), pixelaToken, true, "THANKS123");
        if (!pixelaCreated) {
            throw new PixelaAccountCreationException("Failed to create Pixela account for email: " + request.getEmail(), null);
        }

        // Creación de la foto de perfil
        Photo photo = Photo.builder()
                .url("https://firebasestorage.googleapis.com/v0/b/habitoplus-6d438.appspot.com/o/DefaultImage.jpg?alt=media")
                .build();

        // Creación del perfil
        Profile profile = Profile.builder()
                .age(request.getAge())
                .dateOfRegistration(formattedDate)
                .status(true)
                .lastConnection(formattedLastConnection)
                .gender(request.getGender())
                .numberPhone(request.getNumberPhone())
                .preferences(request.getPreferences())
                .description(request.getDescription())
                .lastName(request.getLastName())
                .name(request.getName())
                .username(request.getUsername())
                .birthDate(request.getBirthDate())
                .photo(photo)
                .build();
        profileRepository.save(profile);

        // Creación de la racha
        Streak streak = new Streak();
        streak.setProfile(profile);
        streak.setCompleteDays(0);
        streak.setConsecutiveDays(0);
        streak.setStartDate(null);
        streak.setEndDate(null);
        streakRepository.save(streak);

        profileRepository.save(profile);

        // Creación de la cuenta
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleAccounts.ROLE_USER)
                .status(true)
                .profile(profile)
                .build();
        Account savedAccount = accountRepository.save(account);

        // Creación de la entidad Pixela y guardado en la base de datos
        Pixela pixela = new Pixela();
        pixela.setUsername(request.getEmail());
        pixela.setToken(pixelaToken);
        pixela.setAccount(savedAccount);
        pixelaRepository.save(pixela);

        return AuthResponseDTO.builder()
                .token(jwtService.getToken(account))
                .build();
    }

    private String generatePixelaToken() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, 15);
    }

    public void forgotPassword(ForgotPasswordRequestDTO request) {
        String email = request.getEmail();
        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            String token = UUID.randomUUID().toString();
            account.setResetToken(token);
            accountRepository.save(account);

            String subject = "Recuperación de contraseña";
            String resetUrl = "http://localhost:4200/reset-password?token=" + token;
            String text = "Haga clic en el siguiente enlace para recuperar su contraseña: " + resetUrl;
            emailService.sendEmail(email, subject, text);
        } else {
            throw new InvalidCredentialsException("Email not found");
        }
    }

    public void resetPassword(String token, String newPassword) {
        Optional<Account> accountOpt = accountRepository.findByResetToken(token);
        if (accountOpt.isEmpty()) {
            throw new InvalidCredentialsException("Invalid token");
        }

        Account account = accountOpt.get();
        account.setPassword(passwordEncoder.encode(newPassword));
        account.setResetToken(null);
        accountRepository.save(account);
    }
}
