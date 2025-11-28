package com.academic.fh.controller;

import com.academic.fh.model.MovimientoInventario;
import com.academic.fh.service.MovimientoInventarioService;
import com.academic.fh.service.InventarioService;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/movimientos")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;
    private final InventarioService inventarioService;
    private final ProductoService productoService;
    private final UserService userService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoInventarioService,
            InventarioService inventarioService,
            ProductoService productoService,
            UserService userService) {
        this.movimientoInventarioService = movimientoInventarioService;
        this.inventarioService = inventarioService;
        this.productoService = productoService;
        this.userService = userService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("movimientos", movimientoInventarioService.findAll());
        return "admin/movimientos/index";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("movimiento", new MovimientoInventario());
        model.addAttribute("inventarios", inventarioService.findAll());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/movimientos/create";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MovimientoInventario movimiento) {
        movimientoInventarioService.save(movimiento);
        return "redirect:/admin/movimientos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        MovimientoInventario movimiento = movimientoInventarioService.findById(id).orElse(null);
        model.addAttribute("movimiento", movimiento);
        model.addAttribute("inventarios", inventarioService.findAll());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/movimientos/create"; // Reusing create for now if it supports it, or need edit.html
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        MovimientoInventario movimiento = movimientoInventarioService.findById(id).orElse(null);
        model.addAttribute("movimiento", movimiento);
        return "admin/movimientos/show";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        movimientoInventarioService.delete(id);
        return "redirect:/admin/movimientos";
    }
}
