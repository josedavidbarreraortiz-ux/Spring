package com.academic.fh.service;

import com.academic.fh.model.Inventario;
import com.academic.fh.model.MovimientoInventario;
import com.academic.fh.model.Producto;
import com.academic.fh.model.Venta;
import com.academic.fh.repository.InventarioRepository;
import com.academic.fh.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository,
            MovimientoInventarioRepository movimientoInventarioRepository) {
        this.inventarioRepository = inventarioRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> findById(Integer id) {
        return inventarioRepository.findById(id);
    }

    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    /**
     * Guarda el inventario y registra un movimiento si hay cambio de stock
     */
    public Inventario saveWithMovimiento(Inventario inventario, String tipo, String motivo) {
        Integer stockAnterior = 0;
        Integer stockNuevo = inventario.getInventarioStockActual();

        // Si es actualización, obtener stock anterior
        if (inventario.getInventarioId() != null) {
            Optional<Inventario> existente = inventarioRepository.findById(inventario.getInventarioId());
            if (existente.isPresent()) {
                stockAnterior = existente.get().getInventarioStockActual();
            }
        }

        Inventario saved = inventarioRepository.save(inventario);

        // Registrar movimiento si hay diferencia de stock
        int diferencia = stockNuevo - stockAnterior;
        if (diferencia != 0) {
            registrarMovimiento(
                    saved.getProducto(),
                    tipo,
                    Math.abs(diferencia),
                    stockAnterior,
                    stockNuevo,
                    motivo,
                    null);
        }

        return saved;
    }

    public void delete(Integer id) {
        inventarioRepository.deleteById(id);
    }

    // Productos sin stock
    public List<Inventario> findSinStock() {
        return inventarioRepository.findSinStock();
    }

    // Productos con stock crítico
    public List<Inventario> findStockCritico() {
        return inventarioRepository.findStockCritico();
    }

    // Contar productos sin stock
    public long countSinStock() {
        return inventarioRepository.countSinStock();
    }

    // Contar productos con stock crítico
    public long countStockCritico() {
        return inventarioRepository.countStockCritico();
    }

    /**
     * Busca el inventario de un producto específico
     */
    public Optional<Inventario> findByProductoId(Integer productoId) {
        return inventarioRepository.findAll().stream()
                .filter(inv -> inv.getProducto() != null && inv.getProducto().getProductoId().equals(productoId))
                .findFirst();
    }

    /**
     * Reduce el stock de un producto en el inventario (usado en ventas)
     */
    public void reducirStock(Integer productoId, Integer cantidad) {
        Optional<Inventario> inventarioOpt = findByProductoId(productoId);

        if (inventarioOpt.isPresent()) {
            Inventario inventario = inventarioOpt.get();
            Integer stockAnterior = inventario.getInventarioStockActual();

            if (stockAnterior >= cantidad) {
                Integer stockNuevo = stockAnterior - cantidad;
                inventario.setInventarioStockActual(stockNuevo);
                inventarioRepository.save(inventario);

                // Registrar movimiento de salida
                registrarMovimiento(
                        inventario.getProducto(),
                        "SALIDA",
                        cantidad,
                        stockAnterior,
                        stockNuevo,
                        "Venta realizada",
                        null);
            } else {
                throw new RuntimeException("Stock insuficiente para el producto ID: " + productoId);
            }
        }
    }

    /**
     * Aumenta el stock de un producto (entrada de mercancía)
     */
    public void aumentarStock(Integer productoId, Integer cantidad, String motivo) {
        Optional<Inventario> inventarioOpt = findByProductoId(productoId);

        if (inventarioOpt.isPresent()) {
            Inventario inventario = inventarioOpt.get();
            Integer stockAnterior = inventario.getInventarioStockActual();
            Integer stockNuevo = stockAnterior + cantidad;

            inventario.setInventarioStockActual(stockNuevo);
            inventarioRepository.save(inventario);

            // Registrar movimiento de entrada
            registrarMovimiento(
                    inventario.getProducto(),
                    "ENTRADA",
                    cantidad,
                    stockAnterior,
                    stockNuevo,
                    motivo != null ? motivo : "Entrada de mercancía",
                    null);
        }
    }

    /**
     * Registra un movimiento de inventario
     */
    private void registrarMovimiento(Producto producto, String tipo, Integer cantidad,
            Integer stockAnterior, Integer stockNuevo,
            String motivo, Venta venta) {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setMovimientoTipo(tipo);
        movimiento.setMovimientoCantidad(cantidad);
        movimiento.setMovimientoStockAnterior(stockAnterior);
        movimiento.setMovimientoStockNuevo(stockNuevo);
        movimiento.setMovimientoMotivo(motivo);
        movimiento.setMovimientoFecha(LocalDate.now());
        movimiento.setVenta(venta);

        movimientoInventarioRepository.save(movimiento);
    }
}
