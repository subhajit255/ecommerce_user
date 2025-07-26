package com.service.user.service;

import com.service.user.dto.AuthResponseDto;
import com.service.user.dto.UserDto;
import com.service.user.entities.User;
import com.service.user.request.UserRegistrationRequest;

import java.util.UUID;

public interface AuthService {
    AuthResponseDto register(UserRegistrationRequest userRegistrationRequest);
    AuthResponseDto login(String email, String password);
    AuthResponseDto googleLogin(String token);
    UserDto myProfile(UUID userId);
    User getUser(UUID userId);
}
