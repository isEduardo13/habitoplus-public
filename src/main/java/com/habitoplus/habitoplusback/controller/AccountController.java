package com.habitoplus.habitoplusback.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Optional;

import com.habitoplus.habitoplusback.dto.UpdateAccountDTO;
import com.habitoplus.habitoplusback.model.Account;
import com.habitoplus.habitoplusback.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@RestController
@RequestMapping("accounts")
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
@Tag(name = "Accounts", description = "Provides all the operations related to the account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get account by Email")
    @ApiResponse(responseCode = "200", description = "Return account by email", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))})
    @GetMapping("/byEmail/{email}")
    public ResponseEntity<Account> getAccountByEmail(@PathVariable  @Valid     @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email") String email) {
        Optional<Account> account = accountService.getAccountByEmail(email);
        return account.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get account by Id")
    @ApiResponse(responseCode = "200", description = "Return account by id", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))})
    @GetMapping("/{idAccount}")
    public ResponseEntity<Account> getAccountById(@PathVariable @Valid @Min(value = 1, message = "Id must be greater than or equal to 1")  Integer idAccount) {
        Account account= accountService.getAccountById(idAccount);  
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }

    @Operation(summary = "Update account")
    @ApiResponse(responseCode = "200", description = "", content = {
            @Content(mediaType = "application/json" )})
    @PutMapping("/update/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Account> updateAccount(@PathVariable @Valid @Min(value = 1, message = "ID must be greater than or equal to 1") Integer id, @Valid @RequestBody UpdateAccountDTO account) {
        Account updatedAccount = accountService.updateAccount(id,account);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @Operation(summary = "Delete account")
    @ApiResponse(responseCode = "204", description = "Change status to false to deactivate the account")
    @PutMapping("/delete/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteAccount(@PathVariable @Valid @Min(1) @Min(value = 1, message = "ID must be greater than or equal to 1") Integer id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "logout")
    @ApiResponse(responseCode = "200", description = "Logout account", content = {
        @Content(mediaType = "application/json")})
@PostMapping("/logout/{id}")
@SecurityRequirement(name = "bearerAuth")
public ResponseEntity<String> logout(@PathVariable @Min(1) @Min(value = 1, message = "ID must be greater than or equal to 1")Integer id) {
    accountService.logout(id);
    return new ResponseEntity<>(new Gson().toJson( "Logged out successfully"), HttpStatus.OK);
}

}
