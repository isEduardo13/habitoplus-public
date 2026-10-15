package com.habitoplus.habitoplusback.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;
    @NotBlank(message = "Age cannot be blank")
    @Size(min = 1, max = 3, message = "Age must be between 1 and 3 characters")
    @Pattern(regexp = "^\\d{1,2}$", message = "Age must be a number")
    private String age;
    @NotBlank(message ="Gender cannot be blank example Male, Female or Other")
    @Pattern(regexp = "Male|male|MALE|M|m|Female|female|FEMALE|F|f|Other|", message="Gender must be Male / male / MALE / M / m\\r\\n\"+ \"Female / female / FEMALE / F / f\\r\\n\" + \"Other")
    @Size(min = 1, max = 17, message = "Gender must be between 1 and 17 characters example Male / male / MALE / M / m\r\n"+ "Female / female / FEMALE / F / f\r\n" + "Other")
    private String gender;
    @NotBlank(message = "Phone number cannot be blank example +country code-number")
    @Pattern(regexp = "^\\+\\d{1,3}\\s?\\d{4,14}$", message = "Phone number must be in the format +country code-number")
    @Size(min = 3, max = 19, message = "Phone number must be between 3 and 19 characters format +country code-number")
    private String numberPhone;
    @Size(max = 100, message = "Preferences accepts a maximum of 100 characters")
    @NotBlank(message = "Preferences cannot be blank")
    private String preferences;
    @Size( max = 100, message = "The description accepts a maximum of 100 characters")
    @NotBlank(message = "The description cannot be blank")
    private String description;

    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String lastName;
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;
    @NotBlank(message = "Birth date cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birth date must be in the format yyyy-mm-dd")
    @Size(min = 1, max = 10, message = "Birth date must be between 1 and 10 characters format yyyy-mm-dd")
    private String birthDate;
}
