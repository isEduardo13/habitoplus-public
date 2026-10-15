package com.habitoplus.habitoplusback.controller;


import com.google.gson.Gson;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import com.habitoplus.habitoplusback.dto.NoteDTO;
import com.habitoplus.habitoplusback.dto.UpdateNoteDTO;
import com.habitoplus.habitoplusback.model.Note;
import com.habitoplus.habitoplusback.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
 

@RestController
@Tag(name = "Notes", description = "Provides all the operations related to the Note")
@RequestMapping("notes")
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/registerNote")
    @Operation(summary = "Create a new note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Note created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid note data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> createNote(@Valid @RequestBody NoteDTO noteDTO) {
        noteService.addNote(noteDTO);
        return new ResponseEntity<>(new Gson().toJson("Saved successfully"), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all notes by profile ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found notes"),
            @ApiResponse(responseCode = "404", description = "Notes not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/profiles/{profileId}")
    public Page<Note> getAllPaginated(@PathVariable @Min(value = 1, message = "ID must be greater than or equal to 1") Integer profileId,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return noteService.getAll(profileId, page, pageSize);
    }
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get a note by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found note"),
            @ApiResponse(responseCode = "404", description = "Note not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{idNote}")
    @SecurityRequirement(name = "bearerAuth")
    public Note getNoteById(@PathVariable @Min(value = 1, message = "ID must be greater than or equal to 1") int idNote) {
        return noteService.getNoteById(idNote);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a note by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{idNote}")
    
    public ResponseEntity<String> deleteNoteById(@PathVariable @Min(value = 1, message = "ID must be greater than or equal to 1") int idNote) {
        noteService.deleteNote(idNote);
        return new ResponseEntity<>(new Gson().toJson( "Delete successfully"), HttpStatus.OK);
    }
    
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a note by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note updated successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{updateNote}")
    public ResponseEntity<String> updateNoteById( @RequestBody @Valid UpdateNoteDTO noteDTO) {
        noteService.updateNoteById(noteDTO);
        return new ResponseEntity<>(new Gson().toJson("Update successfully"), HttpStatus.OK);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
