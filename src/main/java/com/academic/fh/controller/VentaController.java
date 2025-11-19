package com.academic.fh.controller;

import com.academic.fh.model.Venta;
import com.academic.fh.repository.VentaRepository;
import com.academic.fh.repository.ClienteRepository;
import com.academic.fh.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaRepository ventaRepo;
    private final ClienteRepository clienteRepo;
    private final UserRepository userRepo;

    public VentaController(VentaRepository ventaRepo,
                           ClienteRepository clienteRepo,
                           UserRepository userRepo) {
        this.ventaRepo = ventaRepo;
        this.clienteRepo = clienteRepo;
        this.userRepo = userRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaRepo.findAll());
        return "ventas/lista"; // templates/ventas/lista.html
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteRepo.findAll());
        model.addAttribute("usuarios", userRepo.findAll());
        return "ventas/form"; // templates/ventas/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta) {
        ventaRepo.save(venta);
        return "redirect:/ventas";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Venta venta = ventaRepo.findById(id).orElse(null);
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteRepo.findAll());
        model.addAttribute("usuarios", userRepo.findAll());
        return "ventas/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        ventaRepo.deleteById(id);
        return "redirect:/ventas";
    }
}
