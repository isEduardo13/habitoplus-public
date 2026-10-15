package com.habitoplus.habitoplusback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.habitoplus.habitoplusback.model.Streak;
import com.habitoplus.habitoplusback.service.StreakService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

 
@RestController
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET })
@RequestMapping("streaks")
@Tag(name = "Streaks", description = "Provides all the operations related to the streaks")
public class StreakController {
    @Autowired
    private StreakService streakService;

    @Operation(summary = "Get streak by IdProfile")
    @ApiResponse(responseCode = "200", description = "Found streaks")
    @ApiResponse(responseCode = "204", description = "No streaks found")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("profile/{idProfile}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Streak> getStreakByProfile(
            @PathVariable @Min(value = 1, message = "ID must be greater than or equal to 1") Integer idProfile) {
        Streak streak = streakService.getByStreakOfAProfile(idProfile);
        return ResponseEntity.ok(streak);
    }

}
