package com.academic.fh.controller;

import com.academic.fh.model.Producto;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService,
            CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "admin/productos/index";
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        return "admin/productos/create";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        productoService.save(producto);
        return "redirect:/admin/productos";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id).orElse(null);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        return "admin/productos/edit";
    }

    // VER DETALLE
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id).orElse(null);
        model.addAttribute("producto", producto);
        return "admin/productos/show";
    }

    // ELIMINAR
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.delete(id);
        return "redirect:/admin/productos";
    }
}
