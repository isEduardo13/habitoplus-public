package com.habitoplus.habitoplusback.dto;

import java.util.List;

import com.habitoplus.habitoplusback.enums.GroupType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class GroupDTO {
    @NotBlank(message = "DTO: The group name must not be null and must contain at least three characters.")
    @Size(min = 3, max = 50, message = "DTO: The group name must be at most 50 characters long and have at least 3 characters")
    private String name;

    @NotBlank(message = "DTO: The group description must not be null and must contain at least three characters.")
    @Size(min = 3, max = 255, message = "DTO: The group description must be at most 255 characters long and be at least 3 characters long.")
    private String description;

    @NotNull(message= "DTO: The group type must not be null")
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @NotEmpty(message = "DTO: The group must have at least one member.")
    @Size(min = 1, message = "DTO: The group must have at least one member.")
    private List<GroupMemberDTO> groupMembersDTO;
}
