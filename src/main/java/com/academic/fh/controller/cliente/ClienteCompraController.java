package com.academic.fh.controller.cliente;

import com.academic.fh.service.VentaService;
import com.academic.fh.service.CategoriaService;
import com.academic.fh.service.ClienteService;
import com.academic.fh.service.UserService;
import com.academic.fh.util.SecurityUtils;
import com.academic.fh.model.User;
import com.academic.fh.model.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente/compras")
public class ClienteCompraController {

    private final VentaService ventaService;
    private final CategoriaService categoriaService;
    private final ClienteService clienteService;
    private final UserService userService;

    public ClienteCompraController(VentaService ventaService, CategoriaService categoriaService,
            ClienteService clienteService, UserService userService) {
        this.ventaService = ventaService;
        this.categoriaService = categoriaService;
        this.clienteService = clienteService;
        this.userService = userService;
    }

    @GetMapping
    public String compras(Model model) {
        Cliente cliente = getAuthenticatedCliente();

        if (cliente != null) {
            model.addAttribute("compras", ventaService.findByClienteId(cliente.getClienteId()));
        } else {
            model.addAttribute("compras", java.util.Collections.emptyList());
        }

        model.addAttribute("categorias", categoriaService.findAll());
        return "cliente/purchase-list";
    }

    @GetMapping("/{id}")
    public String detalleCompra(@PathVariable Long id, Model model) {
        model.addAttribute("venta", ventaService.findById(id).orElse(null));
        model.addAttribute("categorias", categoriaService.findAll());
        return "cliente/purchase-detail";
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
