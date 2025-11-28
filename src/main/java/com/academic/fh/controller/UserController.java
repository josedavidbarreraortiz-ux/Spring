package com.academic.fh.controller;

import com.academic.fh.model.User;
import com.academic.fh.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", userService.findAll());
        return "admin/usuarios/index";
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("usuario", new User());
        return "admin/usuarios/create";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute User usuario) {
        userService.save(usuario);
        return "redirect:/admin/usuarios";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "admin/usuarios/edit";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/usuarios";
    }
}
