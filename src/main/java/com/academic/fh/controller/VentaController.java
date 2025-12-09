package com.academic.fh.controller;

import com.academic.fh.model.Venta;
import com.academic.fh.model.VentaDetalle;
import com.academic.fh.model.Producto;
import com.academic.fh.model.Cliente;
import com.academic.fh.model.User;
import com.academic.fh.service.VentaService;
import com.academic.fh.service.ClienteService;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.MetodoPagoService;
import com.academic.fh.service.InventarioService;
import com.academic.fh.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final MetodoPagoService metodoPagoService;
    private final InventarioService inventarioService;
    private final UserService userService;

    public VentaController(VentaService ventaService,
            ClienteService clienteService,
            ProductoService productoService,
            MetodoPagoService metodoPagoService,
            InventarioService inventarioService,
            UserService userService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.metodoPagoService = metodoPagoService;
        this.inventarioService = inventarioService;
        this.userService = userService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.findAll());
        return "admin/ventas/index";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        Venta venta = new Venta();
        venta.setVentaFecha(LocalDate.now());
        venta.setVentaHora(LocalTime.now());

        model.addAttribute("venta", venta);
        model.addAttribute("usuarios", userService.findAll());
        model.addAttribute("productos", productoService.findAllActivos());
        model.addAttribute("metodosPago", metodoPagoService.findAll());
        return "admin/ventas/create";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String clienteNombre,
            @RequestParam(required = false) String clienteTelefono,
            @RequestParam(required = false) String clienteDireccion,
            @RequestParam(required = false) List<Integer> productoIds,
            @RequestParam(required = false) List<Integer> cantidades,
            RedirectAttributes redirectAttributes) {

        boolean esNuevo = venta.getVentaId() == null;

        // Establecer fecha y hora actuales
        if (esNuevo) {
            venta.setVentaFecha(LocalDate.now());
            venta.setVentaHora(LocalTime.now());
        }

        // Obtener usuario seleccionado
        User usuario = null;
        if (userId != null) {
            usuario = userService.findById(userId).orElse(null);
            venta.setUser(usuario);
        }

        // Buscar o crear cliente basado en el usuario
        if (usuario != null) {
            Cliente cliente = clienteService.findByUserId(usuario.getId()).orElse(null);
            if (cliente == null) {
                cliente = new Cliente();
                cliente.setUser(usuario);
                cliente.setClienteEmail(usuario.getEmail());
            }
            // Actualizar datos del cliente
            if (clienteNombre != null && !clienteNombre.isEmpty()) {
                cliente.setClienteNombre(clienteNombre);
            }
            if (clienteTelefono != null && !clienteTelefono.isEmpty()) {
                cliente.setClienteTelefono(clienteTelefono);
            }
            if (clienteDireccion != null && !clienteDireccion.isEmpty()) {
                cliente.setClienteDireccion(clienteDireccion);
            }
            cliente = clienteService.save(cliente);
            venta.setCliente(cliente);
        }

        // Calcular total y crear detalles
        BigDecimal total = BigDecimal.ZERO;
        List<VentaDetalle> detalles = new ArrayList<>();

        if (productoIds != null && cantidades != null) {
            for (int i = 0; i < productoIds.size(); i++) {
                if (productoIds.get(i) != null && cantidades.get(i) != null && cantidades.get(i) > 0) {
                    Producto producto = productoService.findById(productoIds.get(i)).orElse(null);
                    if (producto != null) {
                        VentaDetalle detalle = new VentaDetalle();
                        detalle.setProducto(producto);
                        detalle.setVentaDetalleCantidad(cantidades.get(i));
                        detalle.setVentaDetallePrecioVenta(producto.getProductoPrecioVenta());
                        BigDecimal subtotal = producto.getProductoPrecioVenta()
                                .multiply(BigDecimal.valueOf(cantidades.get(i)));
                        detalle.setSubtotal(subtotal);
                        detalle.setVenta(venta);
                        detalles.add(detalle);
                        total = total.add(subtotal);

                        // Reducir inventario
                        try {
                            inventarioService.reducirStock(producto.getProductoId(), cantidades.get(i));
                        } catch (Exception e) {
                            // Ignorar si no hay inventario suficiente
                        }
                    }
                }
            }
        }

        venta.setVentaTotal(total);
        venta.setDetalles(detalles);

        ventaService.save(venta);

        if (esNuevo) {
            redirectAttributes.addFlashAttribute("successMessage", "Venta registrada exitosamente por $" + total);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Venta actualizada exitosamente");
        }

        return "redirect:/admin/ventas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Venta venta = ventaService.findById(id).orElse(null);
        model.addAttribute("venta", venta);
        model.addAttribute("usuarios", userService.findAll());
        model.addAttribute("productos", productoService.findAllActivos());
        model.addAttribute("metodosPago", metodoPagoService.findAll());
        return "admin/ventas/edit";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Venta venta = ventaService.findById(id).orElse(null);
        model.addAttribute("venta", venta);
        return "admin/ventas/show";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ventaService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Venta eliminada exitosamente");
        return "redirect:/admin/ventas";
    }
}
