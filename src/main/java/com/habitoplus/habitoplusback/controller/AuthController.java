package com.habitoplus.habitoplusback.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.habitoplus.habitoplusback.dto.AuthResponseDTO;
import com.habitoplus.habitoplusback.dto.ForgotPasswordRequestDTO;
import com.habitoplus.habitoplusback.dto.LoginRequestDTO;
import com.habitoplus.habitoplusback.dto.RegisterRequestDTO;
import com.habitoplus.habitoplusback.dto.ResetPasswordRequestDTO;
import com.habitoplus.habitoplusback.service.AuthService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST , RequestMethod.GET})
@Tag(name = "Authentication", description = "Provides all the operations related to the authentication")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "login")
    @ApiResponse(responseCode = "200", description = "Allows a registered user to log in to the system by providing their credentials (username/email and password). If authentication is successful, the system returns an authentication token, enabling the user to access protected resources", content = {
            @Content(mediaType = "application/json") })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {

        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "Allows a new user to create an account in the system.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RegisterRequestDTO.class))) })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody  @Valid RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Forgot password")
    @ApiResponse(responseCode = "200", description = "Allows a user to initiate the password reset process by providing their registered email address. If the email is found in the system, an email with a password reset link is sent to the user.", content = {
            @Content(mediaType = "application/json", array=@ArraySchema(schema = @Schema(implementation = ForgotPasswordRequestDTO.class))) })
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO request) {
        authService.forgotPassword(request);
        return new ResponseEntity<>(new Gson().toJson("Password reset code sent to email"), HttpStatus.OK);
    }
    @Operation(summary = "Reset password")
    @ApiResponse(responseCode = "200", description ="Allows a user to reset their password using a valid reset token sent to their email. The user must provide a new password and confirm it. If successful, the user's password is updated in the system.", content = {
            @Content(mediaType = "application/json", array=@ArraySchema(schema = @Schema(implementation = ResetPasswordRequestDTO.class))) })
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @Valid @RequestBody ResetPasswordRequestDTO request) {
        System.out.println("Reset password" + request.getNewPassword());
        authService.resetPassword(token, request.getNewPassword());
        return new ResponseEntity<>(new Gson().toJson("Password has been reset successfully"),HttpStatus.OK);
    }

    @Operation(summary = "Get current user authorities")
    @ApiResponse(responseCode = "200", description = "Return the current user authorities", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))})
    
    @Hidden
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/authorities")
    public ResponseEntity<?> getCurrentUserAuthorities(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authenticated user");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
    @Hidden
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
    return new ResponseEntity<>((new Gson().toJson("pong")), HttpStatus.OK);
}
    
}
