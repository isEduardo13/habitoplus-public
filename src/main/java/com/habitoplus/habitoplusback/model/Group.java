package com.habitoplus.habitoplusback.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.habitoplus.habitoplusback.enums.GroupType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`groups`")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGroup;

    @NotBlank(message = "The group name must not be null and must contain at least three characters.")
    @Size(min = 3, max = 50, message = "The group name must be at most 50 characters long and have at least 3 characters")
    @Column(nullable = false, name = "name")
    private String name;

    @NotBlank(message = "The group description must not be null and must contain at least three characters.")
    @Size(min = 3, max = 255, message = "The group description must be at most 255 characters long and be at least 3 characters long.")
    @Column(nullable = false, name = "description")
    private String description;

    @NotNull(message= "The group type must not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    private GroupType groupType;

    @NotNull(message = "The creation date cannot be null")
    @Column(nullable = false, name = "creation_date")
    private Date creationDate;

    @Column(name = "url")
    private String url;

    @Size(min = 1, message = "The group must have at least one member.")
    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> members;
}
