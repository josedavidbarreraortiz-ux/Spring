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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public VentaService(
            VentaRepository ventaRepository,
            VentaDetalleRepository ventaDetalleRepository,
            ClienteRepository clienteRepository,
            MetodoPagoRepository metodoPagoRepository,
            ProductoService productoService) {

        this.ventaRepository = ventaRepository;
        this.ventaDetalleRepository = ventaDetalleRepository;
        this.clienteRepository = clienteRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.productoService = productoService;
    }

    /**
     * Crear una venta completa con sus detalles
     */
    public Venta crearVenta(Long clienteId, Long metodoPagoId, List<Map<String, Object>> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Obtener cliente y método de pago
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

        // Calcular total
        double total = calcularTotalVenta(carrito);

        // Crear venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setMetodoPago(metodoPago);
        venta.setVentaFecha(java.time.LocalDate.now());
        venta.setVentaHora(java.time.LocalTime.now());
        venta.setVentaTotal(java.math.BigDecimal.valueOf(total));

        // Guardar venta
        venta = ventaRepository.save(venta);

        // Crear detalles de venta
        for (Map<String, Object> item : carrito) {
            Long productoId = Long.valueOf(item.get("id").toString());
            Integer cantidad = Integer.valueOf(item.get("cantidad").toString());
            Double precio = Double.valueOf(item.get("precio").toString());

            Producto producto = productoService.getProductoOrThrow(productoId);

            VentaDetalle detalle = new VentaDetalle();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setVentaDetalleCantidad(cantidad);
            detalle.setVentaDetallePrecioVenta(java.math.BigDecimal.valueOf(precio));
            detalle.setSubtotal(java.math.BigDecimal.valueOf(cantidad * precio));

            ventaDetalleRepository.save(detalle);
        }

        return venta;
    }

    /**
     * Calcular el total de una venta
     */
    public double calcularTotalVenta(List<Map<String, Object>> carrito) {
        return carrito.stream()
                .mapToDouble(item -> {
                    Integer cantidad = Integer.valueOf(item.get("cantidad").toString());
                    Double precio = Double.valueOf(item.get("precio").toString());
                    return cantidad * precio;
                })
                .sum();
    }

    /**
     * Buscar venta por ID
     */
    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    /**
     * Obtener venta o lanzar excepción
     */
    public Venta getVentaOrThrow(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    /**
     * Listar todas las ventas
     */
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    /**
     * Guardar o actualizar venta
     */
    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    /**
     * Eliminar venta
     */
    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

    /**
     * Obtener detalles de una venta
     */
    public List<VentaDetalle> getDetallesByVenta(Long ventaId) {
        Venta venta = getVentaOrThrow(ventaId);
        return ventaDetalleRepository.findAll().stream()
                .filter(d -> d.getVenta().getVentaId().equals(ventaId))
                .toList();
    }
}
