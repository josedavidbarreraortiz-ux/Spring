package com.academic.fh.controller;

import com.academic.fh.model.Producto;
import com.academic.fh.model.Categoria;
import com.academic.fh.repository.ProductoRepository;
import com.academic.fh.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository,
                              CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "productos/lista"; // templates/productos/lista.html
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos/form";  // templates/productos/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        productoRepository.save(producto);
        return "redirect:/productos";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoRepository.deleteById(id);
        return "redirect:/productos";
    }
}
