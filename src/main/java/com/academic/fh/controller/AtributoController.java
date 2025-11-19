package com.academic.fh.controller;

import com.academic.fh.model.Atributo;
import com.academic.fh.repository.AtributoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/atributos")
public class AtributoController {

    private final AtributoRepository atributoRepo;

    public AtributoController(AtributoRepository atributoRepo) {
        this.atributoRepo = atributoRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("atributos", atributoRepo.findAll());
        return "atributos/lista"; // templates/atributos/lista.html
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("atributo", new Atributo());
        return "atributos/form"; // templates/atributos/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Atributo atributo) {
        atributoRepo.save(atributo);
        return "redirect:/atributos";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Atributo atributo = atributoRepo.findById(id).orElse(null);
        model.addAttribute("atributo", atributo);
        return "atributos/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        atributoRepo.deleteById(id);
        return "redirect:/atributos";
    }
}
