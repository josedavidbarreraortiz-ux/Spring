package com.academic.fh.service;

import com.academic.fh.model.Inventario;
import com.academic.fh.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> findById(Long id) {
        return inventarioRepository.findById(id);
    }

    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public void delete(Long id) {
        inventarioRepository.deleteById(id);
    }

    /**
     * Verificar disponibilidad de stock para un producto
     */
    public boolean verificarDisponibilidad(Long productoId, int cantidadRequerida) {
        List<Inventario> inventarios = inventarioRepository.findAll();

        int stockTotal = inventarios.stream()
                .filter(inv -> inv.getProducto().getProductoId().equals(productoId))
                .mapToInt(inv -> inv.getInventarioStockActual())
                .sum();

        return stockTotal >= cantidadRequerida;
    }

    /**
     * Obtener stock total de un producto
     */
    public int obtenerStockTotal(Long productoId) {
        List<Inventario> inventarios = inventarioRepository.findAll();

        return inventarios.stream()
                .filter(inv -> inv.getProducto().getProductoId().equals(productoId))
                .mapToInt(inv -> inv.getInventarioStockActual())
                .sum();
    }
}
