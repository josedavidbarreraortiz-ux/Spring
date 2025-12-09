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
        // Mostrar solo productos activos en la página principal
        model.addAttribute("productos", productoService.findAllActivos());
        model.addAttribute("categorias", categoriaService.findAll());
        return "index";
    }

    @GetMapping("/mi-cuenta")
    public String miCuenta(org.springframework.security.core.Authentication authentication) {
        // Si no está autenticado, redirigir al login
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Redirigir según el rol del usuario
        if (authentication.getAuthorities()
                .contains(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (authentication.getAuthorities()
                .contains(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))) {
            return "redirect:/cliente/dashboard";
        }

        // Por defecto, redirigir al login
        return "redirect:/login";
    }
}
