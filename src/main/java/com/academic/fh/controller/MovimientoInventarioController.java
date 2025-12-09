package com.academic.fh.controller;

import com.academic.fh.model.MovimientoInventario;
import com.academic.fh.service.MovimientoInventarioService;
import com.academic.fh.service.InventarioService;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/movimientos")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;
    private final InventarioService inventarioService;
    private final ProductoService productoService;
    private final UserService userService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoInventarioService,
            InventarioService inventarioService,
            ProductoService productoService,
            UserService userService) {
        this.movimientoInventarioService = movimientoInventarioService;
        this.inventarioService = inventarioService;
        this.productoService = productoService;
        this.userService = userService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Integer productoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {

        List<MovimientoInventario> movimientos;

        // Normalizar strings vacíos a null para evitar problemas con filtros vacíos del
        // formulario
        if (tipo != null && tipo.trim().isEmpty()) {
            tipo = null;
        }

        // Si hay algún filtro aplicado, usar consulta con filtros
        if (tipo != null || productoId != null || fechaInicio != null || fechaFin != null) {
            movimientos = movimientoInventarioService.findByFiltros(tipo, productoId, fechaInicio, fechaFin);
        } else {
            movimientos = movimientoInventarioService.findAll();
        }

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("productos", productoService.findAll());

        // Mantener filtros seleccionados
        model.addAttribute("tipo", tipo);
        model.addAttribute("productoId", productoId);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        // Estadísticas
        model.addAttribute("totalMovimientos", movimientoInventarioService.countTotal());
        model.addAttribute("totalEntradas", movimientoInventarioService.countByTipo("ENTRADA"));
        model.addAttribute("totalSalidas", movimientoInventarioService.countByTipo("SALIDA"));
        model.addAttribute("totalAjustes", movimientoInventarioService.countByTipo("AJUSTE"));

        return "admin/movimientos/index";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("movimiento", new MovimientoInventario());
        model.addAttribute("inventarios", inventarioService.findAll());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/movimientos/create";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute MovimientoInventario movimiento) {
        movimientoInventarioService.save(movimiento);
        return "redirect:/admin/movimientos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        MovimientoInventario movimiento = movimientoInventarioService.findById(id).orElse(null);
        model.addAttribute("movimiento", movimiento);
        model.addAttribute("inventarios", inventarioService.findAll());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/movimientos/create"; // Reusing create for now if it supports it, or need edit.html
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        MovimientoInventario movimiento = movimientoInventarioService.findById(id).orElse(null);
        model.addAttribute("movimiento", movimiento);
        return "admin/movimientos/show";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        movimientoInventarioService.delete(id);
        return "redirect:/admin/movimientos";
    }
}
