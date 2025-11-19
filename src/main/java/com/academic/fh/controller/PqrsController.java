package com.academic.fh.controller;

import com.academic.fh.model.PQRS;
import com.academic.fh.repository.PqrsRepository;
import com.academic.fh.repository.ClienteRepository;
import com.academic.fh.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pqrs")
public class PqrsController {

    private final PqrsRepository pqrsRepo;
    private final ClienteRepository clienteRepo;
    private final UserRepository userRepo;

    public PqrsController(PqrsRepository pqrsRepo,
                          ClienteRepository clienteRepo,
                          UserRepository userRepo) {
        this.pqrsRepo = pqrsRepo;
        this.clienteRepo = clienteRepo;
        this.userRepo = userRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pqrsList", pqrsRepo.findAll());
        return "pqrs/lista"; // templates/pqrs/lista.html
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("pqrs", new PQRS());
        model.addAttribute("clientes", clienteRepo.findAll());
        model.addAttribute("usuarios", userRepo.findAll());
        return "pqrs/form"; // templates/pqrs/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute PQRS pqrs) {
        pqrsRepo.save(pqrs);
        return "redirect:/pqrs";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        PQRS pqrs = pqrsRepo.findById(id).orElse(null);
        model.addAttribute("pqrs", pqrs);
        model.addAttribute("clientes", clienteRepo.findAll());
        model.addAttribute("usuarios", userRepo.findAll());
        return "pqrs/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        pqrsRepo.deleteById(id);
        return "redirect:/pqrs";
    }
}
