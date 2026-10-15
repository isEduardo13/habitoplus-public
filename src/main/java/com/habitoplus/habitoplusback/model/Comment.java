package com.habitoplus.habitoplusback.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "comments")
@IdClass(CommentPK.class)
public class Comment {

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

    @Id
    @NotNull(message = "Date time cannot be null")
    @Column(nullable = false, name = "date_time")
    private LocalDateTime dateTime;

    @NotBlank(message = "The content must not be empty")
    @Size(min = 1, message = "The description must have a minimum of 1 character.")
    @Column(nullable = false, name = "content")
    private String content;
}
