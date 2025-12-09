package com.academic.fh.controller;

import com.academic.fh.model.Inventario;
import com.academic.fh.service.InventarioService;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/inventario")
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;
    private final UserService userService;

    public InventarioController(InventarioService inventarioService,
            ProductoService productoService,
            UserService userService) {
        this.inventarioService = inventarioService;
        this.productoService = productoService;
        this.userService = userService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("inventarios", inventarioService.findAll());
        model.addAttribute("sinStock", inventarioService.findSinStock());
        model.addAttribute("stockCritico", inventarioService.findStockCritico());
        model.addAttribute("countSinStock", inventarioService.countSinStock());
        model.addAttribute("countStockCritico", inventarioService.countStockCritico());
        return "admin/inventario/index";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("inventario", new Inventario());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/inventario/create";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Inventario inventario,
            @RequestParam(required = false, defaultValue = "Actualización manual") String motivo,
            RedirectAttributes redirectAttributes) {
        boolean esNuevo = inventario.getInventarioId() == null;

        // Obtener el nombre del producto desde la BD
        String nombreProducto = "Inventario";
        if (inventario.getProducto() != null && inventario.getProducto().getProductoId() != null) {
            var producto = productoService.findById(inventario.getProducto().getProductoId());
            if (producto.isPresent()) {
                nombreProducto = producto.get().getProductoNombre();
                // Asignar el producto completo al inventario
                inventario.setProducto(producto.get());
            }
        }

        // Determinar tipo de movimiento según cambio de stock
        String tipoMovimiento = "AJUSTE";
        if (!esNuevo) {
            var existente = inventarioService.findById(inventario.getInventarioId());
            if (existente.isPresent()) {
                int stockAnterior = existente.get().getInventarioStockActual();
                int stockNuevo = inventario.getInventarioStockActual();
                if (stockNuevo > stockAnterior) {
                    tipoMovimiento = "ENTRADA";
                } else if (stockNuevo < stockAnterior) {
                    tipoMovimiento = "SALIDA";
                }
            }
        } else {
            // Si es nuevo y tiene stock > 0, es una entrada inicial
            if (inventario.getInventarioStockActual() > 0) {
                tipoMovimiento = "ENTRADA";
                motivo = "Stock inicial del producto";
            }
        }

        // Usar saveWithMovimiento para registrar el movimiento
        inventarioService.saveWithMovimiento(inventario, tipoMovimiento, motivo);

        if (esNuevo) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Inventario para '" + nombreProducto + "' creado exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Inventario para '" + nombreProducto + "' actualizado exitosamente");
        }

        return "redirect:/admin/inventario";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        Inventario inventario = inventarioService.findById(id).orElse(null);
        model.addAttribute("inventario", inventario);
        return "admin/inventario/show";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Inventario inventario = inventarioService.findById(id).orElse(null);
        model.addAttribute("inventario", inventario);
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        return "admin/inventario/edit";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Inventario inventario = inventarioService.findById(id).orElse(null);
        String nombreProducto = inventario != null && inventario.getProducto() != null
                ? inventario.getProducto().getProductoNombre()
                : "el registro";

        inventarioService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Inventario de '" + nombreProducto + "' eliminado");

        return "redirect:/admin/inventario";
    }
}
