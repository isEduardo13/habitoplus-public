package com.habitoplus.habitoplusback.dto;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDTO {

    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
    @Digits(integer = 2, fraction = 7, message = "Latitude must have at most 2 digits before decimal point and 7 after")
    private Double latitude;


    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
    @Digits(integer = 3, fraction = 7, message = "Longitude must have at most 3 digits before decimal point and 7 after")
    private Double longitude;

}
