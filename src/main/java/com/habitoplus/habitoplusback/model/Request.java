package com.habitoplus.habitoplusback.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.habitoplus.habitoplusback.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
@IdClass(RequestPK.class)
public class Request {

    @Id
    @NotNull(message = "Profile cannot be null")
    @OneToOne
    @JoinColumn(name = "id_profile", referencedColumnName = "idProfile", nullable = false)
    private Profile profile;


    @Id
    @NotNull(message = "Group cannot be null")
    @JsonBackReference()
    @OneToOne()
    @JoinColumn(name = "id_group", referencedColumnName = "idGroup", nullable = false)
    private Group group;

    @NotNull(message = "Date request cannot be null")
    @Column(nullable = false, name = "date_request")
    private Date dateRequest;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    private Status status;
}
