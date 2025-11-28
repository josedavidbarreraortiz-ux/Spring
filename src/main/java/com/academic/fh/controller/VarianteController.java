package com.academic.fh.controller;

import com.academic.fh.model.ProductoVariante;
import com.academic.fh.service.VarianteService;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.AtributoService;
import com.academic.fh.service.AtributoValorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/variantes")
public class VarianteController {

    private final VarianteService varianteService;
    private final ProductoService productoService;
    private final AtributoService atributoService;
    private final AtributoValorService atributoValorService;

    public VarianteController(VarianteService varianteService,
            ProductoService productoService,
            AtributoService atributoService,
            AtributoValorService atributoValorService) {
        this.varianteService = varianteService;
        this.productoService = productoService;
        this.atributoService = atributoService;
        this.atributoValorService = atributoValorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("variantes", varianteService.findAll());
        return "variantes/lista";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("variante", new ProductoVariante());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("atributos", atributoService.findAll());
        model.addAttribute("valores", atributoValorService.findAll());
        return "variantes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ProductoVariante variante) {
        varianteService.save(variante);
        return "redirect:/variantes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        ProductoVariante variante = varianteService.findById(id).orElse(null);
        model.addAttribute("variante", variante);
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("atributos", atributoService.findAll());
        model.addAttribute("valores", atributoValorService.findAll());
        return "variantes/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        varianteService.delete(id);
        return "redirect:/variantes";
    }
}
