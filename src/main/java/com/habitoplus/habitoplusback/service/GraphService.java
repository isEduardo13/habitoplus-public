package com.habitoplus.habitoplusback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.api.services.storage.Storage.Projects.HmacKeys.Create;
import com.habitoplus.habitoplusback.dto.GraphCreateDTO;
import com.habitoplus.habitoplusback.exception.GraphPostException;
import com.habitoplus.habitoplusback.exception.PixelaConnectionException;
import com.habitoplus.habitoplusback.exception.PixelaGraphCreationException;
import com.habitoplus.habitoplusback.exception.PixelaIOException;
import com.habitoplus.habitoplusback.exception.TokenNotFoundException;
import com.habitoplus.habitoplusback.model.Graph;
import com.habitoplus.habitoplusback.model.Habit;
import com.habitoplus.habitoplusback.model.Pixela;
import com.habitoplus.habitoplusback.repository.GraphRepository;
import com.habitoplus.habitoplusback.repository.HabitRepository;
import com.habitoplus.habitoplusback.repository.PixelaRepository;

import jakarta.transaction.Transactional;

@Service
public class GraphService {
    @Autowired
    private GraphRepository graphRepository;
    @Autowired
    private PixelaRepository pixelaRepository;

    @Autowired
    private HabitRepository habitRepository;

    public void saveGraphDetails(String graphId, String graphName, String unit, String type, String color,
            Pixela pixela) {
        Graph graph = new Graph();
        graph.setGraphId(graphId);
        graph.setGraphName(graphName);
        graph.setUnit(unit);
        graph.setType(type);
        graph.setColor(color);
        graph.setPixela(pixela);

        graphRepository.save(graph);
    }

    private final String pixelaUrl = "https://pixe.la/v1/users";

    @Transactional
    public boolean createGraphicHabit(GraphCreateDTO graphCreateDTO) throws PixelaConnectionException, PixelaIOException {
        Pixela pixela = pixelaRepository.findByUsername(graphCreateDTO.getUsername())
                .orElseThrow(() -> new TokenNotFoundException("Token not found for username: " + graphCreateDTO.getUsername()));
   
        String username = graphCreateDTO.getUsername().replaceAll("@.*", "");
        String graphId = "habit-" + graphCreateDTO.getIdHabit();
        String graphName = graphCreateDTO.getHabitName();
        String unit = "count";
        String type = "int";
        String color = "shibafu";
        String payload = String.format(
                "{\"id\":\"%s\",\"name\":\"%s\",\"unit\":\"%s\",\"type\":\"%s\",\"color\":\"%s\"}", graphId, graphName,
                unit, type, color);

        RestTemplate restTemplate = new RestTemplate();
        String url = pixelaUrl + "/" + username + "/graphs";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-USER-TOKEN", pixela.getToken());

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new PixelaGraphCreationException("Failed to create graph: " + response.getStatusCode());
        }
        saveGraphDetails(graphId, graphName, unit, type, color, pixela);
        return true;
    }

    public boolean postGraphValue(String username, String graphId, String token, String date, String quantity) {
        String url = pixelaUrl + "/" + username + "/graphs/" + graphId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-TOKEN", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = String.format("{\"date\":\"%s\",\"quantity\":\"%s\"}", date, quantity);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                System.out.println("Failed to post pixel - Status code: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GraphPostException("Error occurred while posting pixel: " + e.getMessage());
        }
    }

    /*
     * public Optional<Graph> getGraphByHabitId(Integer habitId){
     * return graphRepository.findByGraphId("habit-" + habitId);
     * }
     */
}
