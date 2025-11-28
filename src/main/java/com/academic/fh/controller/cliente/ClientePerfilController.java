package com.academic.fh.controller.cliente;

import com.academic.fh.service.UserService;
import com.academic.fh.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente/perfil")
public class ClientePerfilController {

    private final UserService userService;
    private final ClienteService clienteService;

    public ClientePerfilController(UserService userService, ClienteService clienteService) {
        this.userService = userService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String perfil(Model model) {
        // TODO: Obtener clienteId del usuario autenticado
        Long clienteId = 1L;
        com.academic.fh.model.Cliente cliente = clienteService.findById(clienteId).orElse(null);

        model.addAttribute("cliente", cliente);
        return "cliente/perfil";
    }

    @GetMapping("/editar")
    public String editarPerfil(Model model) {
        // TODO: Obtener clienteId del usuario autenticado
        Long clienteId = 1L;
        com.academic.fh.model.Cliente cliente = clienteService.findById(clienteId).orElse(null);

        model.addAttribute("cliente", cliente);
        return "cliente/perfil-editar";
    }

    @PostMapping("/editar")
    public String guardarPerfil(
            @RequestParam String nombre,
            @RequestParam String telefono) {

        // TODO: Obtener clienteId del usuario autenticado
        Long clienteId = 1L;
        com.academic.fh.model.Cliente cliente = clienteService.findById(clienteId).orElse(null);

        // Actualizar datos del usuario y cliente
        if (cliente != null && cliente.getUser() != null) {
            cliente.getUser().setName(nombre);
            cliente.setClienteTelefono(telefono);
            clienteService.save(cliente);
        }

        return "redirect:/cliente/perfil";
    }

    @GetMapping("/cambiar-password")
    public String cambiarPasswordForm() {
        return "cliente/cambiar-password";
    }

    @PostMapping("/cambiar-password")
    public String cambiarPassword(
            @RequestParam String actual,
            @RequestParam String nueva) {

        // TODO: Obtener userId del usuario autenticado
        Long userId = 1L;
        com.academic.fh.model.User user = userService.findById(userId).orElse(null);

        if (user == null || !user.getPassword().equals(actual)) {
            return "redirect:/cliente/perfil/cambiar-password?error=1";
        }

        user.setPassword(nueva);
        userService.save(user);

        return "redirect:/cliente/perfil";
    }
}
