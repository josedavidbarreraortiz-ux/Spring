package com.academic.fh.controller.cliente;

import com.academic.fh.service.UserService;
import com.academic.fh.service.ClienteService;
import com.academic.fh.service.CategoriaService;
import com.academic.fh.util.SecurityUtils;
import com.academic.fh.model.User;
import com.academic.fh.model.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente/perfil")
public class ClientePerfilController {

    private final UserService userService;
    private final ClienteService clienteService;
    private final CategoriaService categoriaService;

    public ClientePerfilController(UserService userService, ClienteService clienteService,
            CategoriaService categoriaService) {
        this.userService = userService;
        this.clienteService = clienteService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String perfil(Model model) {
        Cliente cliente = getAuthenticatedCliente();
        model.addAttribute("cliente", cliente);
        model.addAttribute("categorias", categoriaService.findAll());
        return "cliente/perfil";
    }

    @GetMapping("/editar")
    public String editarPerfil(Model model) {
        // TODO: Obtener clienteId del usuario autenticado
        Integer clienteId = 1;
        com.academic.fh.model.Cliente cliente = clienteService.findById(clienteId).orElse(null);

        model.addAttribute("cliente", cliente);
        model.addAttribute("categorias", categoriaService.findAll());
        return "cliente/perfil-editar";
    }

    @PostMapping("/editar")
    public String guardarPerfil(
            @RequestParam String nombre,
            @RequestParam String telefono) {

        // TODO: Obtener clienteId del usuario autenticado
        Integer clienteId = 1;
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
    public String cambiarPasswordForm(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
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

    private Cliente getAuthenticatedCliente() {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        if (userEmail != null) {
            User user = userService.findByEmail(userEmail).orElse(null);
            if (user != null) {
                return clienteService.findByUserId(user.getId()).orElse(null);
            }
        }
        return null;
    }
}
