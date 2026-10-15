package com.habitoplus.habitoplusback.model;

import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Integer idNotification;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Message cannot be blank")
    @Size(max = 500, message = "Message must not exceed 500 characters")
    private String message;

    @Column(name = "type", length = 50, nullable = false)
    @NotBlank(message = "Type cannot be blank")
    private String type;

    @Column(name = "notification_date", nullable = false)
    @NotNull(message = "Date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd")
    private Date dateNotification;

    @Column(name = "is_read", columnDefinition = "BIT(1)", nullable = false)
    @NotNull(message = "Read status cannot be null")
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "id_habit", nullable = false)
    @JsonBackReference
    private Habit habit;
}
