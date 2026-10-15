package com.habitoplus.habitoplusback.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;

public final class FirebaseCredentialsProvider {

    private FirebaseCredentialsProvider() {
    }

    public static Credentials getCredentials() throws IOException {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            Path path = Paths.get(credentialsPath);
            if (Files.exists(path)) {
                return GoogleCredentials.fromStream(Files.newInputStream(path));
            }
        }

        return GoogleCredentials.getApplicationDefault();
    }
}
