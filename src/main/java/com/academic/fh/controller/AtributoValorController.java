package com.academic.fh.controller;

import com.academic.fh.model.AtributoValor;
import com.academic.fh.repository.AtributoValorRepository;
import com.academic.fh.repository.AtributoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/valores-atributo")
public class AtributoValorController {

    private final AtributoValorRepository valorRepo;
    private final AtributoRepository atributoRepo;

    public AtributoValorController(AtributoValorRepository valorRepo,
                                   AtributoRepository atributoRepo) {
        this.valorRepo = valorRepo;
        this.atributoRepo = atributoRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("valores", valorRepo.findAll());
        return "atributoValores/lista"; // templates/atributoValores/lista.html
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("valor", new AtributoValor());
        model.addAttribute("atributos", atributoRepo.findAll());
        return "atributoValores/form"; // templates/atributoValores/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute AtributoValor valor) {
        valorRepo.save(valor);
        return "redirect:/valores-atributo";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        AtributoValor valor = valorRepo.findById(id).orElse(null);
        model.addAttribute("valor", valor);
        model.addAttribute("atributos", atributoRepo.findAll());
        return "atributoValores/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        valorRepo.deleteById(id);
        return "redirect:/valores-atributo";
    }
}
