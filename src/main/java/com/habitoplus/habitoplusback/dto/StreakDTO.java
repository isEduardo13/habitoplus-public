package com.habitoplus.habitoplusback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreakDTO {
    @NotNull(message = "The streak ID cannot be null")
    private Integer idStreak;

    @NotNull(message = "The consecutive days cannot be null")
    private Integer consecutiveDays;
}
