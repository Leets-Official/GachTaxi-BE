package com.gachtaxi.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.adminSdk}")
    private String adminSdkPath;

    @PostConstruct
    public void initialize() {

        try {
            InputStream inputStream = getInputStream(adminSdkPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase 설정 완료");
            }

        } catch (IOException e) {
            log.warn("Firebase 설정 중 예외 발생", e);
        }
    }

    private InputStream getInputStream(String path) throws IOException {
        if (new File(path).exists()) {
            return new FileInputStream(path);
        } else {
            return new ClassPathResource(path).getInputStream();
        }
    }
}
