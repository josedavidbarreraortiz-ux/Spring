package com.academic.fh.controller;

import com.academic.fh.model.Inventario;
import com.academic.fh.model.Producto;
import com.academic.fh.repository.InventarioRepository;
import com.academic.fh.repository.ProductoRepository;
import com.academic.fh.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final UserRepository userRepository;

    public InventarioController(InventarioRepository inventarioRepository,
                                ProductoRepository productoRepository,
                                UserRepository userRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
        this.userRepository = userRepository;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("inventarios", inventarioRepository.findAll());
        return "inventario/lista";  // templates/inventario/lista.html
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("inventario", new Inventario());
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("usuarios", userRepository.findAll());
        return "inventario/form";  // templates/inventario/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Inventario inventario) {
        inventarioRepository.save(inventario);
        return "redirect:/inventario";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Inventario inventario = inventarioRepository.findById(id).orElse(null);
        model.addAttribute("inventario", inventario);
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("usuarios", userRepository.findAll());
        return "inventario/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        inventarioRepository.deleteById(id);
        return "redirect:/inventario";
    }
}
