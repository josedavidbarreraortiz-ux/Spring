package com.academic.fh.controller.publico;

import com.academic.fh.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductoService productoService;

    public HomeController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Mostrar productos destacados
        model.addAttribute("productos", productoService.findAll());
        return "index";
    }
}
