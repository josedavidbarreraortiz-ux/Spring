package com.academic.fh.controller.publico;

import com.academic.fh.model.Cliente;
import com.academic.fh.model.User;
import com.academic.fh.service.VentaService;
import com.academic.fh.service.MetodoPagoService;
import com.academic.fh.service.CarritoService;
import com.academic.fh.service.ClienteService;
import com.academic.fh.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public CheckoutController(
            VentaService ventaService,
            MetodoPagoService metodoPagoService,
            CarritoService carritoService,
            ClienteService clienteService,
            UserRepository userRepository) {

        this.ventaService = ventaService;
        this.metodoPagoService = metodoPagoService;
        this.carritoService = carritoService;
        this.clienteService = clienteService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String checkout(Model model, HttpSession session) {

        List<Map<String, Object>> carrito = carritoService.getCarrito(session);

        model.addAttribute("carrito", carrito);
        model.addAttribute("metodosPago", metodoPagoService.findAll());

        // Calcular total para la vista
        double total = 0;
        for (var item : carrito) {
            Double precio = Double.valueOf(item.get("precio").toString());
            Integer cantidad = Integer.valueOf(item.get("cantidad").toString());
            total += precio * cantidad;
        }
        model.addAttribute("totalCarrito", total);
        
        // Obtener usuario autenticado y pasarlo al modelo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User usuarioLogueado = userRepository.findByEmailIgnoreCase(auth.getName()).orElse(null);
            model.addAttribute("usuarioLogueado", usuarioLogueado);
        }
        
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

        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User usuarioActual = null;

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            usuarioActual = userRepository.findByEmailIgnoreCase(auth.getName()).orElse(null);
        }

        Cliente cliente;

        // Variable final para usar en lambda
        final User userParaCliente = usuarioActual;

        // 1. Si hay usuario autenticado, buscar su cliente asociado
        if (usuarioActual != null) {
            cliente = clienteService.findByUserId(usuarioActual.getId())
                    .orElseGet(() -> {
                        // Crear nuevo cliente vinculado al usuario
                        Cliente nuevoCliente = new Cliente();
                        nuevoCliente.setClienteNombre(nombre);
                        nuevoCliente.setClienteEmail(email);
                        nuevoCliente.setClienteTelefono(telefono);
                        nuevoCliente.setClienteNumeroDocumento(documento);
                        nuevoCliente.setClienteDireccion(direccion);
                        nuevoCliente.setUser(userParaCliente); // ← VINCULAR USUARIO (variable final)
                        return clienteService.save(nuevoCliente);
                    });

            // Actualizar datos del cliente existente si cambiaron
            if (cliente.getUser() != null && cliente.getUser().getId().equals(usuarioActual.getId())) {
                cliente.setClienteNombre(nombre);
                cliente.setClienteEmail(email);
                cliente.setClienteTelefono(telefono);
                cliente.setClienteNumeroDocumento(documento);
                cliente.setClienteDireccion(direccion);
                cliente = clienteService.save(cliente);
            }
        } else {
            // 2. Usuario anónimo - buscar por documento o crear
            cliente = clienteService.findByDocumento(documento)
                    .orElseGet(() -> {
                        Cliente nuevoCliente = new Cliente();
                        nuevoCliente.setClienteNombre(nombre);
                        nuevoCliente.setClienteEmail(email);
                        nuevoCliente.setClienteTelefono(telefono);
                        nuevoCliente.setClienteNumeroDocumento(documento);
                        nuevoCliente.setClienteDireccion(direccion);
                        return clienteService.save(nuevoCliente);
                    });
        }

        // Crear venta
        var venta = ventaService.crearVenta(cliente.getClienteId().longValue(), metodoPagoId, carrito);

        // Vaciar carrito
        carritoService.vaciarCarrito(session);

        return "redirect:/cliente/compras/" + venta.getVentaId();
    }
}
