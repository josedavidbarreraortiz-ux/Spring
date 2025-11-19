package com.academic.fh.controller;

import com.academic.fh.model.Pago;
import com.academic.fh.repository.PagoRepository;
import com.academic.fh.repository.VentaRepository;
import com.academic.fh.repository.MetodoPagoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoRepository pagoRepo;
    private final VentaRepository ventaRepo;
    private final MetodoPagoRepository metodoPagoRepo;

    public PagoController(PagoRepository pagoRepo,
                          VentaRepository ventaRepo,
                          MetodoPagoRepository metodoPagoRepo) {
        this.pagoRepo = pagoRepo;
        this.ventaRepo = ventaRepo;
        this.metodoPagoRepo = metodoPagoRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pagos", pagoRepo.findAll());
        return "pagos/lista"; // templates/pagos/lista.html
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("pago", new Pago());
        model.addAttribute("ventas", ventaRepo.findAll());
        model.addAttribute("metodosPago", metodoPagoRepo.findAll());
        return "pagos/form"; // templates/pagos/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pago pago) {
        pagoRepo.save(pago);
        return "redirect:/pagos";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Pago pago = pagoRepo.findById(id).orElse(null);
        model.addAttribute("pago", pago);
        model.addAttribute("ventas", ventaRepo.findAll());
        model.addAttribute("metodosPago", metodoPagoRepo.findAll());
        return "pagos/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        pagoRepo.deleteById(id);
        return "redirect:/pagos";
    }
}
