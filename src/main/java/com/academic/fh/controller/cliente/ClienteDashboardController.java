package com.academic.fh.controller.cliente;

import com.academic.fh.service.VentaService;
import com.academic.fh.service.ClienteService;
import com.academic.fh.service.CategoriaService;
import com.academic.fh.service.UserService;
import com.academic.fh.util.SecurityUtils;
import com.academic.fh.model.User;
import com.academic.fh.model.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteDashboardController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final CategoriaService categoriaService;
    private final UserService userService;

    public ClienteDashboardController(VentaService ventaService, ClienteService clienteService,
            CategoriaService categoriaService, UserService userService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.categoriaService = categoriaService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Obtener el email del usuario autenticado
        String userEmail = SecurityUtils.getCurrentUserEmail();

        if (userEmail != null) {
            // Buscar el User por email
            User user = userService.findByEmail(userEmail).orElse(null);

            if (user != null) {
                // Buscar el Cliente asociado al User
                Cliente cliente = clienteService.findByUserId(user.getId()).orElse(null);

                if (cliente != null) {
                    model.addAttribute("cliente", cliente);
                    // Buscar solo las compras del cliente autenticado
                    model.addAttribute("comprasRecientes", ventaService.findByClienteId(cliente.getClienteId()));
                } else {
                    model.addAttribute("cliente", null);
                    model.addAttribute("comprasRecientes", java.util.Collections.emptyList());
                }
            }
        }

        model.addAttribute("categorias", categoriaService.findAll());
        return "cliente/dashboard";
    }
}
