package com.habitoplus.habitoplusback.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.habitoplus.habitoplusback.dto.GraphCreateDTO;
import com.habitoplus.habitoplusback.exception.GraphPixelaException;
import com.habitoplus.habitoplusback.service.GraphService;
import com.habitoplus.habitoplusback.service.PixelaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Graph", description = "Provides all the operations related to the habit graphs")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class GraphController {

    @Autowired
    private PixelaService pixelaService;
    @Autowired
    private GraphService graphService;

    @GetMapping("/graphs")
    @Operation(summary = "Get a Graph for username and graphId")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> getGraphForHabit(
    @RequestParam String username,
    @RequestParam String graphId) {

    String graphData = pixelaService.getGraph(username, graphId);
    return ResponseEntity.ok(graphData);
    }

    // @GetMapping("/graphs")
    // public ResponseEntity<String> getGraphForHabit(@RequestParam String username, @RequestParam String graphId) {
    //     System.out.println("Recibiendo username: " + username + " y graphId: " + graphId);
    //     // Agrega validación aquí para asegurarte de que los parámetros están bien
    //     // formados
    //     try {
    //         String graphData = pixelaService.getGraph(username, graphId);
    //         return ResponseEntity.ok(graphData);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body("Error al obtener la gráfica: " + e.getMessage());
    //     }
    // }

    // @GetMapping("/getGraphIdByHabitId/{habitId}")
    // @Operation(summary = "Get graphId for a specific habit")
    // @SecurityRequirement(name = "bearerAuth")
    // public ResponseEntity<Map<String, String>> getGraphIdByHabitId(@PathVariable
    // Long habitId) {
    // try {
    // // Llamada al servicio para obtener el graphId asociado al habitId
    // String graphId = pixelaService.getGraphIdByHabitId(habitId);
    // Map<String, String> response = new HashMap<>();
    // response.put("graphId", graphId);

    // return ResponseEntity.ok(response);
    // } catch (GraphNotFoundException e) {
    // // Si no se encuentra el Graph asociado
    // return ResponseEntity.status(HttpStatus.NOT_FOUND)
    // .body(Map.of("error", "Graph not found for the provided habit ID: " +
    // e.getMessage()));
    // } catch (GraphPixelaException e) {
    // // Excepción cuando hay un error con Pixela
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(Map.of("error", "Error with Pixela: " + e.getMessage()));
    // } catch (Exception e) {
    // // Error inesperado
    // e.printStackTrace(); // Esto te ayudará a obtener más detalles del error
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
    // }
    // }

    
    @PostMapping("/createGraph")
    @Operation(summary = "Create a new graph for a habit")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Return the current user authorities", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = GraphCreateDTO.class)) })
    public ResponseEntity<String> createGraphForHabit(@Valid @RequestBody GraphCreateDTO graphCreateDTO) {
        try {
            boolean created = graphService.createGraphicHabit(graphCreateDTO);
            if (created) {
                return ResponseEntity.ok(new Gson().toJson("Graph created successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new Gson().toJson("Graph creation failed"));
            }
        } catch (GraphPixelaException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Gson().toJson(Map.of("error", "Error with Pixela: " + e.getMessage())));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Gson().toJson(Map.of("error", "An unexpected error occurred: " + e.getMessage())));
        }
    }

}
