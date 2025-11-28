package com.academic.fh.controller.cliente;

import com.academic.fh.service.VentaService;
import com.academic.fh.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteDashboardController {

    private final VentaService ventaService;
    private final ClienteService clienteService;

    public ClienteDashboardController(VentaService ventaService, ClienteService clienteService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // TODO: Obtener clienteId del usuario autenticado
        Integer clienteId = 1;

        model.addAttribute("cliente", clienteService.findById(clienteId).orElse(null));
        // TODO: Implementar método en VentaService para buscar últimas compras por
        // cliente
        model.addAttribute("comprasRecientes", ventaService.findAll());

        return "cliente/dashboard";
    }
}
