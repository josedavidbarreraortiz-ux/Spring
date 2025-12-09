package com.academic.fh.repository;

import com.academic.fh.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    // Ordenar por fecha descendente (más recientes primero)
    List<MovimientoInventario> findAllByOrderByMovimientoFechaDesc();

    // Filtrar por tipo de movimiento
    List<MovimientoInventario> findByMovimientoTipoOrderByMovimientoFechaDesc(String tipo);

    // Filtrar por producto
    List<MovimientoInventario> findByProductoProductoIdOrderByMovimientoFechaDesc(Integer productoId);

    // Filtrar por rango de fechas
    List<MovimientoInventario> findByMovimientoFechaBetweenOrderByMovimientoFechaDesc(LocalDate fechaInicio,
            LocalDate fechaFin);

    // Consulta multicriterio con JPQL
    @Query("SELECT m FROM MovimientoInventario m WHERE " +
            "(:tipo IS NULL OR m.movimientoTipo = :tipo) AND " +
            "(:productoId IS NULL OR m.producto.productoId = :productoId) AND " +
            "(:fechaInicio IS NULL OR m.movimientoFecha >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR m.movimientoFecha <= :fechaFin) " +
            "ORDER BY m.movimientoFecha DESC")
    List<MovimientoInventario> findByFiltros(
            @Param("tipo") String tipo,
            @Param("productoId") Integer productoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    // Contar por tipo
    long countByMovimientoTipo(String tipo);
}
