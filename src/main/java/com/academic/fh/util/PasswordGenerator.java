package com.academic.fh.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=== Contraseñas Encriptadas ===");
        System.out.println("admin123: " + encoder.encode("admin123"));
        System.out.println("cliente123: " + encoder.encode("cliente123"));
    }
}
