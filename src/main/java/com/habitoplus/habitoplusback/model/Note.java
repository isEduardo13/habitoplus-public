package com.habitoplus.habitoplusback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_note")
    private int idNote;

    @ManyToOne
    @JoinColumn(name = "id_profile", nullable = false)
    @NotNull(message = "Profile cannot be null")
    @JsonProperty("profile")
    @JsonBackReference
    private Profile profile;
    
    @Column(name = "title", length = 255, nullable = false)
    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Content cannot be empty")
    private String content;

    @Column(name = "date", nullable = false)
    @NotNull(message = "Date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd")
    private String date;
}
