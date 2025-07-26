package com.service.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    @NotBlank(message = "first name should not be blank")
    private String firstName;
    @NotBlank(message = "last name should not be blank")
    private String lastName;
    @NotBlank(message = "email should not be blank")
    @Email(message = "should be a valid email address")
    private String email;
    private String phoneCode;
    private String phone;
    @NotBlank(message = "password is required")
    private String password;
}
