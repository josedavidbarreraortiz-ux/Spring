package com.academic.fh.controller;

import com.academic.fh.model.User;
import com.academic.fh.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", userRepository.findAll());
        return "usuarios/lista"; // templates/usuarios/lista.html
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("usuario", new User());
        return "usuarios/form"; // templates/usuarios/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute User usuario) {
        userRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        User usuario = userRepository.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/usuarios";
    }
}
