package com.habitoplus.habitoplusback.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.habitoplus.habitoplusback.dto.LocationDTO;
import com.habitoplus.habitoplusback.dto.UpdateProfileDTO;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("profiles")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
                RequestMethod.DELETE })
@Tag(name = "Profiles", description = "Provides all the operations related to the profile")
public class ProfileController {

        @Autowired
        private ProfileService profileService;

        @Operation(summary = "Get profile by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found profile", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Profile.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Invalid profile ID", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "404", description = "Profile not found", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                        @Content
                        })
        })
        @GetMapping("{id}")
        @SecurityRequirement(name = "bearerAuth")
        public ResponseEntity<Profile> getProfileById(@PathVariable @Min(1) @Min(value = 1, message = "ID must be greater than or equal to 1") Integer id) {
                Profile profile = profileService.getProfileById(id);
                return new ResponseEntity<>(profile, HttpStatus.OK);
        }
        @Operation(summary = "update profile")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Profile created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Profile.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Invalid profile data", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                        @Content
                        })
        })
        @PutMapping("{id}")
        @SecurityRequirement(name = "bearerAuth")
        public ResponseEntity<Profile> updateProfile(  @PathVariable @Valid @Min(value = 1, message = "ID must be greater than or equal to 1") Integer id, @RequestBody @Valid  UpdateProfileDTO profile) {
                Profile updatedProfile = profileService.updateProfile(id, profile);
                return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
        }


        @Operation(summary= "Get all profiles with pagination")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found profiles", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Profile.class))
                        }),
                        @ApiResponse(responseCode = "404", description = "Profiles not found", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                                        @Content
                        })
        })
        @GetMapping
        @SecurityRequirement(name = "bearerAuth")
        public Page<Profile> getAllProfiles(@RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size) {
                return profileService.getAllProfilesPaginated(page, size);
        }
        @Operation(summary = "Update location")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Location updated successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDTO.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Invalid location data", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "404", description = "Profile not found", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                                        @Content
                        }),
                        @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                                        @Content
                        })
        })
        @PutMapping("/location/{id}")
        @SecurityRequirement(name = "bearerAuth")
        public ResponseEntity<Profile> updateLocation(@PathVariable @Min(1) @Min(value = 1, message = "ID must be greater than or equal to 1") Integer id, @RequestBody @Valid LocationDTO location) {
                Profile updatedProfile = profileService.updateLocation(id, location);
                return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
        }
        @Operation(summary = "Share location")
        @GetMapping("/location/{id}")
        @SecurityRequirement(name = "bearerAuth")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Location updated successfully", content = {
                                @Content(mediaType = "application/json", schema = @Schema(implementation = LocationDTO.class))
                }),
                @ApiResponse(responseCode = "400", description = "Invalid location data", content = {
                                @Content
                }),
                @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                                @Content
                }),
                @ApiResponse(responseCode = "404", description = "Profile not found", content = {
                                @Content
                }),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                                @Content
                }),
                @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                                @Content
                })
})        
        public ResponseEntity<LocationDTO> ShareLocation(@PathVariable @Min(1) @Min(value = 1, message = "ID must be greater than or equal to 1") Integer id) {
                LocationDTO location = profileService.GetLocation(id);
                return new ResponseEntity<>(location, HttpStatus.OK);
        }

}
