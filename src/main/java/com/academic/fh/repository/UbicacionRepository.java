package com.academic.fh.repository;

import com.academic.fh.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    List<Ubicacion> findByUbicacionEstado(Integer estado);

    List<Ubicacion> findByUbicacionBodega(String bodega);
}
