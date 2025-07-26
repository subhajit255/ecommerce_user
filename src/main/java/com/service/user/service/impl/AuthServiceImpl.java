package com.service.user.service.impl;

import com.service.user.dto.AuthResponseDto;
import com.service.user.dto.UserDto;
import com.service.user.entities.Role;
import com.service.user.entities.User;
import com.service.user.repository.RoleRepository;
import com.service.user.repository.UserRepository;
import com.service.user.request.UserRegistrationRequest;
import com.service.user.service.AuthService;
import com.service.user.service.GoogleAuthService;
import com.service.user.service.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final GoogleAuthService googleAuthService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, JwtService jwtService, GoogleAuthService googleAuthService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.googleAuthService = googleAuthService;
    }

    @Override
    public AuthResponseDto register(UserRegistrationRequest userRegistrationRequest) {
        User user = new User();
        user.setFirstName(userRegistrationRequest.getFirstName());
        user.setLastName(userRegistrationRequest.getLastName());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPhoneCode(userRegistrationRequest.getPhoneCode());
        user.setPhone(userRegistrationRequest.getPhone());
        user.setPassword(encoder.encode(userRegistrationRequest.getPassword()));
        // assign role
        Role userRole = roleRepository.findById("USER")
                .orElseThrow(() -> new RuntimeException("role not found"));
        user.setRoles(Set.of(userRole));
        // save the user info in db and return
        User isUserSaved = userRepository.save(user);
        String token = null;
        UserDto userDto = new UserDto(
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getPhoneCode(),
                userRegistrationRequest.getPhone()
        );
        if (isUserSaved != null) {
            token = jwtService.generateToken(
                    userRegistrationRequest.getEmail(),
                    userRegistrationRequest.getPhone(),
                    isUserSaved.getId()
            );
        }
        return new AuthResponseDto(token, userDto);
    }

    @Override
    public AuthResponseDto login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid Credentials");
        }
        // generate the token
        String token = jwtService.generateToken(user.getEmail(), user.getPhone(), user.getId());
        UserDto userDto = new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneCode(),
                user.getPhone()
        );
        AuthResponseDto authResponseDto = new AuthResponseDto(
                token,
                userDto
        );
        return authResponseDto;
    }

    @Override
    public AuthResponseDto googleLogin(String idTokenString) {
        GoogleIdToken.Payload payload = GoogleAuthService.verifyToken(idTokenString);

        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");

        // Check if user already exists
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // Register new user
            user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user = userRepository.save(user);
        }

        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail(), user.getPhone(), user.getId());

        UserDto userDto = new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneCode(),
                user.getPhone()
        );

        return new AuthResponseDto(token, userDto);
    }


    @Override
    public UserDto myProfile(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> new UserDto(
                    user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhoneCode(),
                        user.getPhone()
                ))
                .orElseGet(UserDto::new);
    }
    @Override
    public User getUser(UUID userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("user not found with that given id" + userId)
        );
    }
}

