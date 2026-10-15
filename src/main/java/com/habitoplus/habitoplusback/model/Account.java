package com.habitoplus.habitoplusback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.habitoplus.habitoplusback.enums.RoleAccounts;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAccount;

    @NotBlank(message = "Email cannot be blank")
    @Size(min = 1, max = 254, message = "Email must be between 1 and 254 characters")
    @Column(unique = true, name = "email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Boolean status;

    @Enumerated(EnumType.STRING)
    RoleAccounts role;
    @Column(name = "reset_token")
    private String  resetToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_Profile", referencedColumnName = "idProfile")
    @JsonProperty("profile")
    @JsonManagedReference
    @JsonIgnore
    private Profile profile;


    public String getUsername() {
        return email;
    }

}
