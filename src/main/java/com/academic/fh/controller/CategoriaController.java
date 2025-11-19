package com.academic.fh.controller;

import com.academic.fh.model.Categoria;
import com.academic.fh.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "categorias/lista";  // templates/categorias/lista.html
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";  // templates/categorias/form.html
    }

    // GUARDAR (CREAR o EDITAR)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        model.addAttribute("categoria", categoria);
        return "categorias/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        categoriaRepository.deleteById(id);
        return "redirect:/categorias";
    }
}
