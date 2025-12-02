package com.academic.fh.repository;

import com.academic.fh.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VentaRepository extends JpaRepository<Venta, Long> {
@Query("SELECT MAX(v.ventaCodigo) FROM Venta v")
Integer findMaxCodigo();


}
