package com.habitoplus.habitoplusback.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Password cannot be blank")
    private String newPassword;

}
