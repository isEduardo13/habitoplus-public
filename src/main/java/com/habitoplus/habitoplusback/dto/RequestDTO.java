package com.habitoplus.habitoplusback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    @NotNull(message = "Group cannot be null")
    private Integer idGroup;

    @NotNull(message = "Profile cannot be null")
    private Integer idProfile;
}

