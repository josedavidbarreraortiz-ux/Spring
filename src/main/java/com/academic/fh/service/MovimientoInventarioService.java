package com.academic.fh.service;

import com.academic.fh.model.MovimientoInventario;
import com.academic.fh.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    public List<MovimientoInventario> findAll() {
        return movimientoInventarioRepository.findAll();
    }

    public Optional<MovimientoInventario> findById(Long id) {
        return movimientoInventarioRepository.findById(id);
    }

    public MovimientoInventario save(MovimientoInventario movimiento) {
        return movimientoInventarioRepository.save(movimiento);
    }

    public void delete(Long id) {
        movimientoInventarioRepository.deleteById(id);
    }
}
