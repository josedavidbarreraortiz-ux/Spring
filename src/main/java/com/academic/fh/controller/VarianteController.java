package com.academic.fh.controller;

import com.academic.fh.model.ProductoVariante;
import com.academic.fh.repository.ProductoVarianteRepository;
import com.academic.fh.repository.ProductoRepository;
import com.academic.fh.repository.AtributoRepository;
import com.academic.fh.repository.AtributoValorRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/variantes")
public class VarianteController {

    private final ProductoVarianteRepository varianteRepo;
    private final ProductoRepository productoRepo;
    private final AtributoRepository atributoRepo;
    private final AtributoValorRepository valorRepo;

    public VarianteController(ProductoVarianteRepository varianteRepo,
                              ProductoRepository productoRepo,
                              AtributoRepository atributoRepo,
                              AtributoValorRepository valorRepo) {
        this.varianteRepo = varianteRepo;
        this.productoRepo = productoRepo;
        this.atributoRepo = atributoRepo;
        this.valorRepo = valorRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("variantes", varianteRepo.findAll());
        return "variantes/lista";  // templates/variantes/lista.html
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("variante", new ProductoVariante());
        model.addAttribute("productos", productoRepo.findAll());
        model.addAttribute("atributos", atributoRepo.findAll());
        model.addAttribute("valores", valorRepo.findAll());
        return "variantes/form";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ProductoVariante variante) {
        varianteRepo.save(variante);
        return "redirect:/variantes";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        ProductoVariante variante = varianteRepo.findById(id).orElse(null);
        model.addAttribute("variante", variante);
        model.addAttribute("productos", productoRepo.findAll());
        model.addAttribute("atributos", atributoRepo.findAll());
        model.addAttribute("valores", valorRepo.findAll());
        return "variantes/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        varianteRepo.deleteById(id);
        return "redirect:/variantes";
    }
}
