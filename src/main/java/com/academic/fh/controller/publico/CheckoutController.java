package com.academic.fh.controller.publico;

import com.academic.fh.service.VentaService;
import com.academic.fh.service.MetodoPagoService;
import com.academic.fh.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final VentaService ventaService;
    private final MetodoPagoService metodoPagoService;
    private final CarritoService carritoService;

    public CheckoutController(
            VentaService ventaService,
            MetodoPagoService metodoPagoService,
            CarritoService carritoService) {

        this.ventaService = ventaService;
        this.metodoPagoService = metodoPagoService;
        this.carritoService = carritoService;
    }

    @GetMapping
    public String checkout(Model model, HttpSession session) {

        List<Map<String, Object>> carrito = carritoService.getCarrito(session);

        model.addAttribute("carrito", carrito);
        model.addAttribute("metodosPago", metodoPagoService.findAll());

        return "checkout";
    }

    @PostMapping
    public String procesarCompra(
            @RequestParam Long metodoPagoId,
            HttpSession session) {

        // obtener usuario logueado (temporal)
        Long clienteId = 1L;

        List<Map<String, Object>> carrito = carritoService.getCarrito(session);

        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/carrito";
        }

        // Crear venta usando el servicio (transaccional)
        var venta = ventaService.crearVenta(clienteId, metodoPagoId, carrito);

        // Vaciar carrito
        carritoService.vaciarCarrito(session);

        return "redirect:/cliente/compras/" + venta.getVentaId();
    }
}
