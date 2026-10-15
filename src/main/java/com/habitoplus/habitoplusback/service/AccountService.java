package com.habitoplus.habitoplusback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.habitoplus.habitoplusback.repository.AccountRepository;
import com.habitoplus.habitoplusback.repository.ProfileRepository;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.habitoplus.habitoplusback.dto.UpdateAccountDTO;
import com.habitoplus.habitoplusback.model.Account;
import com.habitoplus.habitoplusback.model.Profile;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    public Account getAccountById(int id) {
        if (accountRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException();

        }
        return accountRepository.findById(id).get();
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Account updateAccount(int id, UpdateAccountDTO updateAccountDTO) {
        Optional<Account> existingAccountOpt = accountRepository.findById(id);
        if (existingAccountOpt.isEmpty()) {
            throw new NoSuchElementException("Account not found with ID: " + id);
        }

        Account existingAccount = existingAccountOpt.get();
        existingAccount.setEmail(updateAccountDTO.getEmail());
        existingAccount.setPassword(passwordEncoder.encode(updateAccountDTO.getPassword()));
        return accountRepository.save(existingAccount);
    }

    public boolean deleteAccount(int id) {

        if (accountRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException();
        }
        Account existingAccount = accountRepository.findById(id).get();

        if (existingAccount != null) {
            existingAccount.setStatus(false);
            accountRepository.save(existingAccount);
            return true;
        }
        return false;
    }

    public boolean DefinitiveDeleteAccount(int id) {

        if (accountRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException();
        }
        Account existingAccount = accountRepository.findById(id).get();
        profileRepository.delete(existingAccount.getProfile());
        accountRepository.delete(existingAccount);
        return true;
    }

    public void logout(Integer id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        Profile profileValid = optionalProfile.orElseThrow(() -> new NoSuchElementException("Profile not found"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        profileValid.setLastConnection(formattedDate);

        profileRepository.save(profileValid);
    }


}