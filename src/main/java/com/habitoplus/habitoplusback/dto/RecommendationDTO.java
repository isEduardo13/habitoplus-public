package com.habitoplus.habitoplusback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {
    @NotBlank(message = "The habit name cannot be blank")
    @Size(max = 50, message = "The habit name must be less than 50 characters")
    private String habitName;
    @NotBlank(message = "The description cannot be blank")
    @Size(max = 100, message = "The description must be less than 200 characters")
    private String description;
}
