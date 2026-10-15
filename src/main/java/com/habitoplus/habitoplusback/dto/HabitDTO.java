package com.habitoplus.habitoplusback.dto;

import com.habitoplus.habitoplusback.enums.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitDTO {
    @NotNull(message = "The profile ID cannot be null")
    private Integer profileId;

    @NotNull(message = "The category ID cannot be null")
    private Integer categoryId;

    @NotBlank(message = "The description cannot be blank")
    @Size(max = 100, message = "The description must be less than 200 characters")
    private String description;

    @NotNull(message = "The priority cannot be null")
    private Priority priority;

    @NotBlank(message = "The habit name cannot be blank")
    @Size(max = 50, message = "The habit name must be less than 50 characters")
    private String habit_name;
}
