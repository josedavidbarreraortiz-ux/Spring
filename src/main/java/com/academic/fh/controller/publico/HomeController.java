package com.academic.fh.controller.publico;

import com.academic.fh.service.ProductoService;
import com.academic.fh.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public HomeController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Mostrar productos destacados y categorías
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("categorias", categoriaService.findAll());
        return "index";
    }
}
