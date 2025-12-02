package com.academic.fh.security;

import com.academic.fh.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();

        // Validar que el rol no sea null o vacío
        if (role == null || role.trim().isEmpty()) {
            role = "USER"; // Rol por defecto
            System.out.println("⚠️ Usuario " + user.getEmail() + " sin rol, asignando USER por defecto");
        }

        // Normalizar: eliminar espacios y convertir a mayúsculas
        role = role.trim().toUpperCase();

        // Construir la autoridad con el prefijo ROLE_
        String authority = "ROLE_" + role;

        // Log para debugging
        System.out.println("🔐 Usuario: " + user.getEmail() +
                " | Rol BD: '" + user.getRole() +
                "' | Authority: '" + authority + "'");

        return Collections.singletonList(
                new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled() != null && user.getEnabled();
    }

    public User getUser() {
        return user;
    }
}