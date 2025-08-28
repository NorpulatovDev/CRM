package org.example.lms_original.config;

import org.example.lms_original.entity.Role;
import org.example.lms_original.entity.User;
import org.example.lms_original.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.admin.username}")
    private String adminUsername;
    
    @Value("${app.admin.password}")
    private String adminPassword;
    
    @Value("${app.admin.email}")
    private String adminEmail;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            
            userRepository.save(admin);
            System.out.println("Default admin user created: " + adminUsername);
        }
    }
}