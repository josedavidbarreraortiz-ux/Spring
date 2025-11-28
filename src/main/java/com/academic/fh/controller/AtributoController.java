package com.academic.fh.controller;

import com.academic.fh.model.Atributo;
import com.academic.fh.service.AtributoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/atributos")
public class AtributoController {

    private final AtributoService atributoService;

    public AtributoController(AtributoService atributoService) {
        this.atributoService = atributoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("atributos", atributoService.findAll());
        return "atributos/lista";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("atributo", new Atributo());
        return "atributos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Atributo atributo) {
        atributoService.save(atributo);
        return "redirect:/atributos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Atributo atributo = atributoService.findById(id).orElse(null);
        model.addAttribute("atributo", atributo);
        return "atributos/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        atributoService.delete(id);
        return "redirect:/atributos";
    }
}
