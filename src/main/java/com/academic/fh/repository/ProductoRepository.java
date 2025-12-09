package com.academic.fh.repository;

import com.academic.fh.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT DISTINCT p.productoMarca FROM Producto p WHERE p.productoMarca IS NOT NULL AND p.productoMarca <> '' ORDER BY p.productoMarca")
    List<String> findDistinctMarcas();

    @Query(value = "SELECT p.* FROM productos p " +
            "LEFT JOIN venta_detalle vd ON vd.producto_id = p.producto_id " +
            "WHERE p.producto_estado = 'Activo' OR p.producto_estado = 'Disponible' " +
            "GROUP BY p.producto_id " +
            "ORDER BY COALESCE(SUM(vd.venta_detalle_cantidad), 0) DESC", nativeQuery = true)
    List<Producto> findTopSellingProducts();
}
