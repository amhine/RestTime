package com.RestTime.RestTime.config;

import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.Role;
import com.RestTime.RestTime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadAdmin();
    }

    private void loadAdmin() {
        String adminEmail = "admin@resttime.com";

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .nom("System")
                    .prenom("Admin")
                    .email(adminEmail)
                    .motDePasse(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .soldeConges(999.0)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ ADMIN créé avec succès ! Email: " + adminEmail + " / Password: admin123");
        } else {
            System.out.println("ℹ️ L'ADMIN existe déjà.");
        }
    }
}