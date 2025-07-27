package com.service.user.request;

import com.service.user.entities.AddressType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressRequest {
    @NotNull(message = "address type is required")
    private AddressType addressType;
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
    private String country;
    @NotBlank(message = "state is required")
    private String state;
    @NotBlank(message = "city is required")
    private String city;

}
