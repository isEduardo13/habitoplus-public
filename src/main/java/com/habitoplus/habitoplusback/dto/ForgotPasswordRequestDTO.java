package com.habitoplus.habitoplusback.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequestDTO {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email")
    private String email;


}