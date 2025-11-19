package com.academic.fh.controller;

import com.academic.fh.model.MovimientoInventario;
import com.academic.fh.repository.MovimientoInventarioRepository;
import com.academic.fh.repository.InventarioRepository;
import com.academic.fh.repository.ProductoRepository;
import com.academic.fh.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movimientos")
public class MovimientoInventarioController {

    private final MovimientoInventarioRepository movimientoRepo;
    private final InventarioRepository inventarioRepo;
    private final ProductoRepository productoRepo;
    private final UserRepository userRepo;

    public MovimientoInventarioController(MovimientoInventarioRepository movimientoRepo,
                                          InventarioRepository inventarioRepo,
                                          ProductoRepository productoRepo,
                                          UserRepository userRepo) {
        this.movimientoRepo = movimientoRepo;
        this.inventarioRepo = inventarioRepo;
        this.productoRepo = productoRepo;
        this.userRepo = userRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("movimientos", movimientoRepo.findAll());
        return "movimientos/lista"; // templates/movimientos/lista.html
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("movimiento", new MovimientoInventario());
        model.addAttribute("inventarios", inventarioRepo.findAll());
        model.addAttribute("productos", productoRepo.findAll());
        model.addAttribute("usuarios", userRepo.findAll());
        return "movimientos/form"; // templates/movimientos/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MovimientoInventario movimiento) {
        movimientoRepo.save(movimiento);
        return "redirect:/movimientos";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        MovimientoInventario movimiento = movimientoRepo.findById(id).orElse(null);
        model.addAttribute("movimiento", movimiento);
        model.addAttribute("inventarios", inventarioRepo.findAll());
        model.addAttribute("productos", productoRepo.findAll());
        model.addAttribute("usuarios", userRepo.findAll());
        return "movimientos/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        movimientoRepo.deleteById(id);
        return "redirect:/movimientos";
    }
}
