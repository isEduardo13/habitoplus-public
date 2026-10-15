package com.habitoplus.habitoplusback.dto;

import com.google.auto.value.AutoValue.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphCreateDTO {
    @NotBlank(message = "DTO: The username must not be null.")
    @NotNull(message = "DTO: The username must not be null.")
    private String username;
    @NotNull(message = "DTO: The habit id must not be null.")
    private Integer idHabit;
    @NotBlank(message = "DTO: The habit name must not be null.")
    @NotNull(message = "DTO: The habit name must not be null.")
    private String habitName;


}
