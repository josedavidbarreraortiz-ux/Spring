package com.academic.fh.config;

import com.academic.fh.model.User;
import com.academic.fh.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Admin
            User admin = userRepository.findAll().stream()
                    .filter(u -> u.getEmail().equals("admin@fh.com"))
                    .findFirst()
                    .orElse(new User());

            admin.setName("Administrador");
            admin.setEmail("admin@fh.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("✅ Usuario Admin actualizado/creado: admin@fh.com / admin123");

            // Cliente
            User client = userRepository.findAll().stream()
                    .filter(u -> u.getEmail().equals("cliente@fh.com"))
                    .findFirst()
                    .orElse(new User());

            client.setName("Cliente Test");
            client.setEmail("cliente@fh.com");
            client.setPassword(passwordEncoder.encode("cliente123"));
            client.setRole("USER");
            client.setEnabled(true);
            userRepository.save(client);
            System.out.println("✅ Usuario Cliente actualizado/creado: cliente@fh.com / cliente123");
        };
    }
}
