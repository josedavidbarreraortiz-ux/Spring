package com.academic.fh.repository;

import com.academic.fh.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT DISTINCT p.productoMarca FROM Producto p WHERE p.productoMarca IS NOT NULL AND p.productoMarca <> '' ORDER BY p.productoMarca")
    List<String> findDistinctMarcas();

    @Query("SELECT p FROM Producto p LEFT JOIN VentaDetalle vd ON vd.producto = p " +
            "WHERE p.productoEstado = 'Activo' OR p.productoEstado = 'Disponible' " +
            "GROUP BY p ORDER BY COALESCE(SUM(vd.ventaDetalleCantidad), 0) DESC")
    List<Producto> findTopSellingProducts();
}
