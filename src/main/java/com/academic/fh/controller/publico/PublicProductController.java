package com.academic.fh.controller.publico;

import com.academic.fh.service.CategoriaService;
import com.academic.fh.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class PublicProductController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public PublicProductController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listado(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            Model model) {

        var productos = productoService.buscarConFiltros(nombre, categoriaId, precioMin, precioMax);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.findAll());

        return "products";
    }

    @GetMapping("/categoria/{id}")
    public String productosPorCategoria(@PathVariable Integer id, Model model) {
        var productos = productoService.buscarConFiltros(null, id, null, null);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("categoriaActual", categoriaService.findById(id).orElse(null));

        return "products";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {

        var producto = productoService.findById(id).orElse(null);

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());

        return "product-detail";
    }
}
