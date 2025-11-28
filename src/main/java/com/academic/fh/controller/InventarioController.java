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

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("inventarios", inventarioService.findAll());
        return "admin/inventario/index";
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("inventario", new Inventario());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/inventario/create";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Inventario inventario) {
        inventarioService.save(inventario);
        return "redirect:/admin/inventario";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Inventario inventario = inventarioService.findById(id).orElse(null);
        model.addAttribute("inventario", inventario);
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/inventario/edit";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        inventarioService.delete(id);
        return "redirect:/admin/inventario";
    }
}
