package com.academic.fh.controller;

import com.academic.fh.model.MetodoPago;
import com.academic.fh.service.MetodoPagoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/metodos-pago")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("metodos", metodoPagoService.findAll());
        return "metodosPago/lista";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("metodo", new MetodoPago());
        return "metodosPago/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MetodoPago metodo) {
        metodoPagoService.save(metodo);
        return "redirect:/metodos-pago";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        MetodoPago metodo = metodoPagoService.findById(id).orElse(null);
        model.addAttribute("metodo", metodo);
        return "metodosPago/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        metodoPagoService.delete(id);
        return "redirect:/metodos-pago";
    }
}
