package com.academic.fh.service;

import com.academic.fh.model.Cliente;
import com.academic.fh.model.MetodoPago;
import com.academic.fh.model.Producto;
import com.academic.fh.model.Venta;
import com.academic.fh.model.VentaDetalle;
import com.academic.fh.repository.ClienteRepository;
import com.academic.fh.repository.MetodoPagoRepository;
import com.academic.fh.repository.VentaRepository;
import com.academic.fh.repository.VentaDetalleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.academic.fh.model.User;
import com.academic.fh.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class VentaService {

    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ClienteRepository clienteRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final ProductoService productoService;
    private final UserRepository userRepository;
    private final InventarioService inventarioService;

    public VentaService(
            VentaRepository ventaRepository,
            VentaDetalleRepository ventaDetalleRepository,
            ClienteRepository clienteRepository,
            MetodoPagoRepository metodoPagoRepository,
            ProductoService productoService,
            UserRepository userRepository,
            InventarioService inventarioService) {

        this.ventaRepository = ventaRepository;
        this.ventaDetalleRepository = ventaDetalleRepository;
        this.clienteRepository = clienteRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.productoService = productoService;
        this.userRepository = userRepository;
        this.inventarioService = inventarioService;
    }

    public Venta crearVenta(Long clienteId, Long metodoPagoId, List<Map<String, Object>> carrito) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No hay un usuario autenticado");
        }

        String email = auth.getName(); // Spring usa el email como username

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

        Cliente cliente = clienteRepository.findById(clienteId.intValue())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId.intValue())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

        double total = calcularTotalVenta(carrito);

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setMetodoPago(metodoPago);

        venta.setUser(user); // ← AQUÍ SE GUARDA EL USUARIO
        venta.setVentaFecha(LocalDate.now());
        venta.setVentaHora(LocalTime.now());
        venta.setVentaTotal(BigDecimal.valueOf(total));

        venta = ventaRepository.save(venta);

        for (Map<String, Object> item : carrito) {
            Long productoId = Long.valueOf(item.get("id").toString());
            Integer cantidad = Integer.valueOf(item.get("cantidad").toString());
            Double precio = Double.valueOf(item.get("precio").toString());

            Producto producto = productoService.getProductoOrThrow(productoId);

            VentaDetalle detalle = new VentaDetalle();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setVentaDetalleCantidad(cantidad);
            detalle.setVentaDetallePrecioVenta(BigDecimal.valueOf(precio));
            detalle.setSubtotal(BigDecimal.valueOf(cantidad * precio));

            ventaDetalleRepository.save(detalle);

            // Reducir el stock del producto en el inventario
            inventarioService.reducirStock(producto.getProductoId(), cantidad);
        }

        return venta;
    }

    public double calcularTotalVenta(List<Map<String, Object>> carrito) {
        return carrito.stream()
                .mapToDouble(item -> {
                    Integer cantidad = Integer.valueOf(item.get("cantidad").toString());
                    Double precio = Double.valueOf(item.get("precio").toString());
                    return cantidad * precio;
                })
                .sum();
    }

    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta getVentaOrThrow(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    public List<Venta> findByClienteId(Integer clienteId) {
        return ventaRepository.findAll().stream()
                .filter(v -> v.getCliente() != null && v.getCliente().getClienteId().equals(clienteId))
                .toList();
    }

    public List<Venta> findByUserId(Long userId) {
        return ventaRepository.findAll().stream()
                .filter(v -> v.getUser() != null && v.getUser().getId().equals(userId))
                .toList();
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

    public List<VentaDetalle> getDetallesByVenta(Long ventaId) {
        Venta venta = getVentaOrThrow(ventaId);
        return ventaDetalleRepository.findAll().stream()
                .filter(d -> d.getVenta().getVentaId().equals(ventaId.intValue()))
                .toList();
    }

    private Integer generarCodigoVenta() {
        Integer max = ventaRepository.findMaxCodigo();
        return (max == null) ? 1 : max + 1;
    }

}
