package com.academic.fh.controller;

import com.academic.fh.model.VentaDetalle;
import com.academic.fh.repository.VentaDetalleRepository;
import com.academic.fh.repository.VentaRepository;
import com.academic.fh.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/detalle-ventas")
public class VentaDetalleController {

    private final VentaDetalleRepository detalleRepo;
    private final VentaRepository ventaRepo;
    private final ProductoRepository productoRepo;

    public VentaDetalleController(VentaDetalleRepository detalleRepo,
                                 VentaRepository ventaRepo,
                                 ProductoRepository productoRepo) {
        this.detalleRepo = detalleRepo;
        this.ventaRepo = ventaRepo;
        this.productoRepo = productoRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("detalles", detalleRepo.findAll());
        return "detalleVentas/lista"; // templates/detalleVentas/lista.html
    }

    // CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("detalle", new VentaDetalle());
        model.addAttribute("ventas", ventaRepo.findAll());
        model.addAttribute("productos", productoRepo.findAll());
        return "detalleVentas/form"; // templates/detalleVentas/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute VentaDetalle detalle) {
        detalleRepo.save(detalle);
        return "redirect:/detalle-ventas";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        VentaDetalle detalle = detalleRepo.findById(id).orElse(null);
        model.addAttribute("detalle", detalle);
        model.addAttribute("ventas", ventaRepo.findAll());
        model.addAttribute("productos", productoRepo.findAll());
        return "detalleVentas/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        detalleRepo.deleteById(id);
        return "redirect:/detalle-ventas";
    }
}
