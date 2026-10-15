package com.habitoplus.habitoplusback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.habitoplus.habitoplusback.exception.GraphPixelaException;
import com.habitoplus.habitoplusback.exception.InvalidPixelaDataException;
import com.habitoplus.habitoplusback.exception.PixelaAccountCreationException;
import com.habitoplus.habitoplusback.exception.TokenNotFoundException;
import com.habitoplus.habitoplusback.exception.UserAlreadyExistsException;
import com.habitoplus.habitoplusback.model.Account;
import com.habitoplus.habitoplusback.model.Graph;
import com.habitoplus.habitoplusback.model.Habit;
import com.habitoplus.habitoplusback.model.Pixela;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.repository.AccountRepository;
import com.habitoplus.habitoplusback.repository.GraphRepository;
import com.habitoplus.habitoplusback.repository.HabitRepository;
import com.habitoplus.habitoplusback.repository.PixelaRepository;
import com.habitoplus.habitoplusback.repository.ProfileRepository;

@Service
public class PixelaService {

    @Autowired
    private PixelaRepository pixelaRepository;
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private GraphRepository graphRepository;
    @Autowired
    private AccountRepository accountRepository;

    private final String pixelaUrl = "https://pixe.la/v1/users";

    @Transactional
    public boolean createPixelaAccount(String email, String token, boolean isNotMinor, String thanksCode) {
        RestTemplate restTemplate = new RestTemplate();
        String url = pixelaUrl;
        String username = generateValidUsername(email);

        if (!isValidToken(token) || !isValidUsername(username)) {
            throw new InvalidPixelaDataException("Invalid token or username format.", new Throwable());
        }

        String requestJson = createRequestJson(token, username, isNotMinor, thanksCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new PixelaAccountCreationException(
                        "Failed to create Pixela account: " + response.getStatusCode());
            }

            return true;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                String responseBody = e.getResponseBodyAsString();
                if (responseBody.contains("This user already exist.")) {
                    throw new UserAlreadyExistsException(" This user already exist.");
                }
                return false;
            }
            throw new PixelaAccountCreationException("Error al crear la cuenta Pixela: " + e.getStatusCode(), e);
        }
    }

    private String createRequestJson(String token, String username, boolean isNotMinor, String thanksCode) {
        return String.format(
                "{\"token\":\"%s\", \"username\":\"%s\", \"agreeTermsOfService\":\"yes\", \"notMinor\":\"%s\"%s}",
                escapeJson(token), escapeJson(username), isNotMinor ? "yes" : "no",
                (thanksCode != null && !thanksCode.isEmpty())
                        ? String.format(", \"thanksCode\":\"%s\"", escapeJson(thanksCode))
                        : "");
    }

    private boolean isValidToken(String token) {
        return token != null && token.length() >= 8 && token.length() <= 128 && token.matches("[ -~]+");
    }

    private boolean isValidUsername(String username) {
        return username != null && username.matches("[a-z][a-z0-9-]{1,32}");
    }

    public String generateValidUsername(String email) {
        String username = email.split("@")[0].toLowerCase();
        username = username.replaceAll("[^a-z0-9-]", "-");

        if (username.isEmpty()) {
            username = "user";
        } else if (username.length() > 32) {
            username = username.substring(0, 32);
        }

        return username;
    }

    private String escapeJson(String input) {
        return input.replace("\"", "\\\"").replace("\n", "\n");
    }

    public String getGraph(String username, String graphId) {

        String token = getTokenByUsername(username);
    
        username = generateValidUsername(username);
        String url = "https://pixe.la/v1/users/" + username + "/graphs/" + graphId;
    
        System.out.println("URL generada: " + url);
        System.out.println("Token usado: " + token);
    
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-TOKEN", token);
    
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                requestEntity, String.class);
    
        System.out.println("Respuesta de Pixela: " + response.getBody());
    
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new GraphPixelaException("Error retrieving graph: " +
                    response.getStatusCode(),
                    response.getStatusCode().value());
        }
    }
    

    public String getTokenByUsername(String username) {
        System.out.println("Buscando token para username: " + username); // Mensaje de depuración
        Pixela pixela = pixelaRepository.findByUsername(username)
                .orElseThrow(() -> new TokenNotFoundException("Token not found for username: " + username));
        return pixela.getToken();
    }

    public String getGraphIdByHabitId(Long habitId) {
        // Obtener el id_profile del hábito
        Habit habit = habitRepository.findById(habitId.intValue())
                .orElseThrow(() -> new RuntimeException("Habit not found for id: " + habitId));
        Integer profileId = habit.getProfile().getIdProfile();

        // Obtener la cuenta asociada al perfil
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found for id: " + profileId));
        Account account = accountRepository.findByProfile(profile)
                .orElseThrow(() -> new RuntimeException("Account not found for profile"));

        // Obtener el Pixela asociado a la cuenta
        Pixela pixela = pixelaRepository.findByAccountId(account.getIdAccount())
                .orElseThrow(() -> new RuntimeException("Pixela account not found for account"));

        // Obtener el graphId asociado a la cuenta Pixela
        Graph graph = graphRepository.findByPixela_IdPixela(pixela.getIdPixela()) // Cambiado aquí
                .orElseThrow(() -> new RuntimeException("Graph not found for Pixela account"));

        return graph.getGraphId();
    }

}
