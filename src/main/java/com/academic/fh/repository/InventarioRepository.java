package com.academic.fh.repository;

import com.academic.fh.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    com.academic.fh.model.Inventario findByProducto(com.academic.fh.model.Producto producto);

    // Productos sin stock (stock actual = 0)
    @Query("SELECT i FROM Inventario i WHERE i.inventarioStockActual = 0 OR i.inventarioStockActual IS NULL")
    List<Inventario> findSinStock();

    // Productos con stock crítico (stock actual <= stock mínimo)
    @Query("SELECT i FROM Inventario i WHERE i.inventarioStockActual <= i.inventarioStockMinimo AND i.inventarioStockActual > 0")
    List<Inventario> findStockCritico();

    // Contar productos sin stock
    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.inventarioStockActual = 0 OR i.inventarioStockActual IS NULL")
    long countSinStock();

    // Contar productos con stock crítico
    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.inventarioStockActual <= i.inventarioStockMinimo AND i.inventarioStockActual > 0")
    long countStockCritico();
}
