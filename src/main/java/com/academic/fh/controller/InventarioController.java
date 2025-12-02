package com.academic.fh.controller;

import com.academic.fh.model.Inventario;
import com.academic.fh.service.InventarioService;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/inventario")
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;
    private final UserService userService;

    public InventarioController(InventarioService inventarioService,
            ProductoService productoService,
            UserService userService) {
        this.inventarioService = inventarioService;
        this.productoService = productoService;
        this.userService = userService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("inventarios", inventarioService.findAll());
        return "admin/inventario/index";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("inventario", new Inventario());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/inventario/create";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Inventario inventario) {
        inventarioService.save(inventario);
        return "redirect:/admin/inventario";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        Inventario inventario = inventarioService.findById(id).orElse(null);
        model.addAttribute("inventario", inventario);
        return "admin/inventario/show";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Inventario inventario = inventarioService.findById(id).orElse(null);
        model.addAttribute("inventario", inventario);
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/inventario/edit";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        inventarioService.delete(id);
        return "redirect:/admin/inventario";
    }
}
