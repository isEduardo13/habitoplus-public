package com.habitoplus.habitoplusback.controller;

import com.habitoplus.habitoplusback.service.PhotoService;
import com.nimbusds.jose.shaded.gson.Gson;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photos")
@Tag(name = "Photos", description = "Operations related to photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Change photo")
    @ApiResponse(responseCode = "200", description = "Photo changed", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    @PostMapping(value = "/changePhoto/{id}",consumes = {"multipart/form-data"})
    public ResponseEntity<String> changePhoto(@PathVariable("id") @Min(value = 1, message = "ID must be greater than or equal to 1") Integer id, @RequestParam("file") MultipartFile multipartFile) {
        String response = photoService.changePhoto(multipartFile, id);
        return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.OK);
    }
}
