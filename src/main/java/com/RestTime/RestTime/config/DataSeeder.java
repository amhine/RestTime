package com.RestTime.RestTime.config;

import com.RestTime.RestTime.model.entity.Service;
import com.RestTime.RestTime.model.entity.Utilisateur;
import com.RestTime.RestTime.model.enumeration.Role;
import com.RestTime.RestTime.repository.ServiceRepository;
import com.RestTime.RestTime.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadServiceAndAdmin();
    }

    private void loadServiceAndAdmin() {
        Service serviceAdmin = serviceRepository.findByNom("Administration")
                .orElseGet(() -> serviceRepository.save(
                        Service.builder()
                                .nom("Administration")
                                .description("Service dédié à la gestion de la plateforme")
                                .build()
                ));

        // 2. Créer l'Utilisateur ADMIN s'il n'existe pas
        String adminEmail = "admin@resttime.com";
        if (!utilisateurRepository.existsByEmail(adminEmail)) {
            Utilisateur admin = Utilisateur.builder()
                    .nom("System")
                    .prenom("Admin")
                    .email(adminEmail)
                    .motDePasse(passwordEncoder.encode("admin123")) // Mot de passe par défaut
                    .role(Role.ADMIN)
                    .soldeConges(999.0) // Solde illimité ou élevé pour l'admin
                    .service(serviceAdmin)
                    .build();

            utilisateurRepository.save(admin);
            System.out.println("✅ ADMIN créé avec succès ! Email: " + adminEmail + " / Password: admin123");
        } else {
            System.out.println("ℹ️ L'ADMIN existe déjà.");
        }
    }
}