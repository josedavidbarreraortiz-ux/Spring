package com.academic.fh.controller;

import com.academic.fh.model.Venta;
import com.academic.fh.service.VentaService;
import com.academic.fh.service.ClienteService;
import com.academic.fh.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final UserService userService;

    public VentaController(VentaService ventaService,
            ClienteService clienteService,
            UserService userService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.userService = userService;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.findAll());
        return "admin/ventas/index";
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/ventas/create";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta) {
        ventaService.save(venta);
        return "redirect:/admin/ventas";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Venta venta = ventaService.findById(id).orElse(null);
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/ventas/edit";
    }

    // VER DETALLE
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Venta venta = ventaService.findById(id).orElse(null);
        model.addAttribute("venta", venta);
        return "admin/ventas/show";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        ventaService.delete(id);
        return "redirect:/admin/ventas";
    }
}
