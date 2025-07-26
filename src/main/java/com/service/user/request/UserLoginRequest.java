package com.service.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
    @NotBlank(message = "email is required")
    @Email(message = "should be an valid email address")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
