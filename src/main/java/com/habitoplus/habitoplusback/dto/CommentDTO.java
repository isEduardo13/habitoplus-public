package com.habitoplus.habitoplusback.dto;

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
public class CommentDTO{
   @NotNull(message = "Profile cannot be null")
   private Integer idProfile;

   @NotNull(message = "Group cannot be null")
   private Integer idGroup;

   @NotBlank(message = "The content must not be empty")
    @Size(min = 1, message = "The description must have a minimum of 1 character.")
   private String content;
}
