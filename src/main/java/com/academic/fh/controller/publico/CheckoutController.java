package com.academic.fh.controller.publico;

import com.academic.fh.model.Cliente;
import com.academic.fh.service.VentaService;
import com.academic.fh.service.MetodoPagoService;
import com.academic.fh.service.CarritoService;
import com.academic.fh.service.ClienteService;
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
    private final ClienteService clienteService;

    public CheckoutController(
            VentaService ventaService,
            MetodoPagoService metodoPagoService,
            CarritoService carritoService,
            ClienteService clienteService) {

        this.ventaService = ventaService;
        this.metodoPagoService = metodoPagoService;
        this.carritoService = carritoService;
        this.clienteService = clienteService;
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
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String documento,
            @RequestParam String direccion,
            @RequestParam Long metodoPagoId,
            HttpSession session) {

        List<Map<String, Object>> carrito = carritoService.getCarrito(session);

        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/carrito";
        }

        // Buscar o crear cliente
        Cliente cliente = clienteService.findByDocumento(documento)
                .orElseGet(() -> {
                    Cliente nuevoCliente = new Cliente();
                    nuevoCliente.setClienteNombre(nombre);
                    nuevoCliente.setClienteEmail(email);
                    nuevoCliente.setClienteTelefono(telefono);
                    nuevoCliente.setClienteNumeroDocumento(documento);
                    nuevoCliente.setClienteDireccion(direccion);
                    return clienteService.save(nuevoCliente);
                });

        // Crear venta
        var venta = ventaService.crearVenta(cliente.getClienteId().longValue(), metodoPagoId, carrito);

        // Vaciar carrito
        carritoService.vaciarCarrito(session);

        return "redirect:/cliente/compras/" + venta.getVentaId();
    }
}
