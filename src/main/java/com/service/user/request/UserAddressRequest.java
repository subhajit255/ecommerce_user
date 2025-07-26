package com.service.user.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressRequest {
    @NotBlank(message = "full address is required")
    private String fullAddress;
    @NotBlank(message = "latitude is required")
    private String latitude;
    @NotBlank(message = "longitude is required")
    private String longitude;
    @NotNull(message = "Zip code is required")
    @Min(value = 10000, message = "Zip code must be at least 5 digits")
    @Max(value = 999999, message = "Zip code must be at most 6 digits")
    private Integer zipCode;

}
