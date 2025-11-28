package com.academic.fh.controller.cliente;

import com.academic.fh.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente/compras")
public class ClienteCompraController {

    private final VentaService ventaService;

    public ClienteCompraController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public String compras(Model model) {
        // TODO: Obtener clienteId del usuario autenticado
        Integer clienteId = 1;

        // TODO: Implementar método en VentaService para buscar por cliente
        model.addAttribute("compras", ventaService.findAll());

        return "cliente/purchase-list";
    }

    @GetMapping("/{id}")
    public String detalleCompra(@PathVariable Long id, Model model) {
        var venta = ventaService.findById(id).orElse(null);
        model.addAttribute("venta", venta);
        return "cliente/purchase-detail";
    }
}
