package com.habitoplus.habitoplusback.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDTO {
    @Min(value = 1, message="The ID of profile must be an integer with a minimum value of 1")
    @NotNull(message = "Profile cannot be null")
    private Integer idProfile;
    @Min(value = 1, message="The ID of group must be an integer with a minimum value of 1")
    @NotNull(message = "Group cannot be null")
    private Integer idGroup;
}
 