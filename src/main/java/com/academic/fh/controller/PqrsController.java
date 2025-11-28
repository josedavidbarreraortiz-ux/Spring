package com.academic.fh.controller;

import com.academic.fh.model.PQRS;
import com.academic.fh.service.PqrsService;
import com.academic.fh.service.ClienteService;
import com.academic.fh.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pqrs")
public class PqrsController {

    private final PqrsService pqrsService;
    private final ClienteService clienteService;
    private final UserService userService;

    public PqrsController(PqrsService pqrsService,
            ClienteService clienteService,
            UserService userService) {
        this.pqrsService = pqrsService;
        this.clienteService = clienteService;
        this.userService = userService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pqrsList", pqrsService.findAll());
        return "pqrs/lista";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("pqrs", new PQRS());
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "pqrs/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute PQRS pqrs) {
        pqrsService.save(pqrs);
        return "redirect:/pqrs";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        PQRS pqrs = pqrsService.findById(id).orElse(null);
        model.addAttribute("pqrs", pqrs);
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "pqrs/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        pqrsService.delete(id);
        return "redirect:/pqrs";
    }
}
