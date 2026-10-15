package com.habitoplus.habitoplusback.dto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateNoteDTO {
    
    private int idNote;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Content cannot be empty")
    private String content;
  
    
}
