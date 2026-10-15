package com.habitoplus.habitoplusback.dto;

import java.sql.Date;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationDTO {

    @NotNull(message = "IdNotification cannot be null")
    private Integer idNotification;

    @NotNull(message = "Message cannot be null")
    private String message;

    @NotNull(message = "Type cannot be null")
    private String type;

    @NotNull(message = "Date cannot be null")
    private Date dateNotification;

    @NotNull(message = "isRead cannot be null")
    private Boolean isRead;

    @NotNull(message = "IdHabit cannot be null")
    private Integer idHabit;
}
