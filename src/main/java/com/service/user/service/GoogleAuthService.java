package com.service.user.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {
//    @Value("${security.oauth2.client.registration.google:clientId}")
    private static final String CLIENT_ID = "876725572331-7hscip4angk87kf5cl8bn6ec0uf51dq6.apps.googleusercontent.com";

    public static GoogleIdToken.Payload verifyToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(CLIENT_ID)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) throw new RuntimeException("Invalid ID token");
            return idToken.getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Token verification failed", e);
        }
    }
}
