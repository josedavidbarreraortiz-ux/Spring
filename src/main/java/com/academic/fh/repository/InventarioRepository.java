package com.academic.fh.repository;

import com.academic.fh.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    com.academic.fh.model.Inventario findByProducto(com.academic.fh.model.Producto producto);
}
