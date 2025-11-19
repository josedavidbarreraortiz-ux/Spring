package com.academic.fh.controller;

import com.academic.fh.model.MetodoPago;
import com.academic.fh.repository.MetodoPagoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/metodos-pago")
public class MetodoPagoController {

    private final MetodoPagoRepository metodoRepo;

    public MetodoPagoController(MetodoPagoRepository metodoRepo) {
        this.metodoRepo = metodoRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("metodos", metodoRepo.findAll());
        return "metodosPago/lista"; // templates/metodosPago/lista.html
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("metodo", new MetodoPago());
        return "metodosPago/form"; // templates/metodosPago/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MetodoPago metodo) {
        metodoRepo.save(metodo);
        return "redirect:/metodos-pago";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        MetodoPago metodo = metodoRepo.findById(id).orElse(null);
        model.addAttribute("metodo", metodo);
        return "metodosPago/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        metodoRepo.deleteById(id);
        return "redirect:/metodos-pago";
    }
}
