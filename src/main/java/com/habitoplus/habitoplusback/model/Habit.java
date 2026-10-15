package com.habitoplus.habitoplusback.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.habitoplus.habitoplusback.enums.Priority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHabit;

    @NotNull(message = "The category cannot be null")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Category category;

    @NotBlank(message = "The habit name cannot be blank")
    @Size(min = 1, max = 50, message = "The habit name must be less than 50 characters")
    @JsonProperty("name")
    private String habit_name;

    @NotBlank(message = "The description cannot be blank")
    @Size(min = 1, max = 100, message = "The description must be less than 200 characters")
    private String description;

    @NotNull(message = "The status cannot be null")
    private Boolean status;

    @NotNull(message = "The priority cannot be null")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotNull(message = "The profile cannot be null")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProfile")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Profile profile;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> notifications;

    public Profile getProfile() {
        return profile;
    }
    
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    
}