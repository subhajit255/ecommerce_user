package com.service.user.controller;

import com.service.user.dto.AuthResponseDto;
import com.service.user.dto.UserDto;
import com.service.user.entities.User;
import com.service.user.request.UserLoginRequest;
import com.service.user.request.UserRegistrationRequest;
import com.service.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.unprocessableEntity().body(
              Map.of(
                      "status", false,
                      "errors", errors
              )
            );
        }
        try{
            AuthResponseDto isUserRegistered = authService.register(userRegistrationRequest);
            return ResponseEntity.ok(isUserRegistered);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", false,
                            "message", e.getMessage() + " -- " + e.getClass()
                    )
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.unprocessableEntity().body(
                    Map.of(
                            "status", false,
                            "errors", errors
                    )
            );
        }
        try{
            AuthResponseDto authResponseDto = authService.login(userLoginRequest.getEmail(), userLoginRequest.getPassword());
            return ResponseEntity.ok(
                    Map.of(
                            "status", true,
                            "message", "Login successful",
                            "data", authResponseDto
                    )
            );
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", false,
                            "message", e.getMessage() + "--" + e.getClass()
                    )
            );
        }
    }

    @GetMapping("/my-profile")
    public ResponseEntity<?> myProfile(@RequestAttribute UUID userId){
        try{
            UserDto isUser = authService.myProfile(userId);
            return ResponseEntity.ok(isUser);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status", false,
                            "message", e.getMessage() + "--" + e.getClass()
                    )
            );
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        try {
            String idToken = request.get("idToken");
            if (idToken == null || idToken.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("status", false, "message", "Missing ID token")
                );
            }

            AuthResponseDto authResponse = authService.googleLogin(idToken);
            return ResponseEntity.ok(
                    Map.of("status", true, "data", authResponse)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("status", false, "message", e.getMessage())
            );
        }
    }
}
