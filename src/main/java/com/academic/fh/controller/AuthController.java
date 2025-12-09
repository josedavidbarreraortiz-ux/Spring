package com.academic.fh.controller;

import com.academic.fh.model.User;
import com.academic.fh.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }

        if (logout != null) {
            model.addAttribute("message", "Sesión cerrada exitosamente");
        }

        return "login";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password_confirmation,
            RedirectAttributes redirectAttributes) {

        try {
            // Validar que las contraseñas coincidan
            if (!password.equals(password_confirmation)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/login";
            }

            // Verificar si el email ya existe
            if (userService.findByEmail(email).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado");
                return "redirect:/login";
            }

            // Crear nuevo usuario
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole("USER");
            newUser.setEnabled(true); // Asegurar que el usuario esté habilitado

            userService.save(newUser);

            redirectAttributes.addFlashAttribute("message", "Registro exitoso. Por favor inicia sesión");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/login-success")
    public String loginSuccess(Authentication authentication) {
        // Redirigir según el rol del usuario
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/cliente/dashboard";
        }
    }

}
