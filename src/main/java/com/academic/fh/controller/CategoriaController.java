package com.academic.fh.controller;

import com.academic.fh.model.Categoria;
import com.academic.fh.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "admin/categorias/index";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categorias/create";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        categoriaService.save(categoria);
        return "redirect:/admin/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.findById(id).orElse(null);
        model.addAttribute("categoria", categoria);
        return "admin/categorias/edit";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.findById(id).orElse(null);
        model.addAttribute("categoria", categoria);
        return "admin/categorias/show";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        categoriaService.delete(id);
        return "redirect:/admin/categorias";
    }
}
