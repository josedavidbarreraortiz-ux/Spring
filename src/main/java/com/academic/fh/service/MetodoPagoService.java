package com.academic.fh.service;

import com.academic.fh.model.MetodoPago;
import com.academic.fh.repository.MetodoPagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    public MetodoPagoService(MetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    public List<MetodoPago> findAll() {
        return metodoPagoRepository.findAll();
    }

    public Optional<MetodoPago> findById(Long id) {
        return metodoPagoRepository.findById(id);
    }

    public MetodoPago save(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    public void delete(Long id) {
        metodoPagoRepository.deleteById(id);
    }
}
