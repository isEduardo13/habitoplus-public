package com.habitoplus.habitoplusback.model;


import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProfile;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Size(max = 50, message = "Last name accepts a maximum of 50 characters")
    private String lastName;

    @NotBlank(message = "Birth date cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birth date must be in the format yyyy-mm-dd")
    @Size(min = 1, max = 10, message = "Birth date must be between 1 and 10 characters format yyyy-mm-dd")
    private String birthDate;

    @NotBlank(message = "Age cannot be blank")
    @Size(min = 1, max = 3, message = "Age must be between 1 and 3 characters")
    @Pattern(regexp = "^\\d{1,2}$", message = "Age must be a number")
    private String age;
    @NotBlank(message ="Gender cannot be blank example Male, Female or Not prefer to say")
    @Pattern(regexp = "Male|male|MALE|M|m|Female|female|FEMALE|F|f|Not prefer to say|", message="Gender must be Male / male / MALE / M / m\\r\\n\"+ \"Female / female / FEMALE / F / f\\r\\n\" + \"Not prefer to say")
    @Size(min = 1, max = 17, message = "Gender must be between 1 and 17 characters example Male / male / MALE / M / m\r\n"+ "Female / female / FEMALE / F / f\r\n" + "Not prefer to say")
    private String gender;

    @Size(max = 100, message = "Preferences accepts a maximum of 100 characters")
    private String preferences;
    @Size( max = 100, message = "The description accepts a maximum of 100 characters")
    private String description;

    @NotBlank(message = "Phone number cannot be blank example +country code-number")
    @Pattern(regexp = "^\\+\\d{1,3}[-\\s]?\\d{4,14}$", message = "Phone number must be in the format +country code-number")
    @Size(min = 3, max = 19, message = "Phone number must be between 3 and 19 characters format +country code-number")
    private String numberPhone;

    @Column(name = "status")
    private Boolean status;

    @NotBlank(message = "Date of registration cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of registration must be in the format yyyy-mm-dd")
    @Size(min = 1, max = 10, message = "Date of registration must be between 1 and 10 characters")
    private String dateOfRegistration;

    private String lastConnection;

    private Double latitude;

    private Double longitude;

    @JoinColumn(name = "id_photo", referencedColumnName = "idPhoto")
    @OneToOne( cascade = CascadeType.ALL )
    private Photo photo;


}
