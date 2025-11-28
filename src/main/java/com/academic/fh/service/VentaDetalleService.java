package com.academic.fh.service;

import com.academic.fh.model.VentaDetalle;
import com.academic.fh.repository.VentaDetalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaDetalleService {

    private final VentaDetalleRepository ventaDetalleRepository;

    public VentaDetalleService(VentaDetalleRepository ventaDetalleRepository) {
        this.ventaDetalleRepository = ventaDetalleRepository;
    }

    public List<VentaDetalle> findAll() {
        return ventaDetalleRepository.findAll();
    }

    public Optional<VentaDetalle> findById(Integer id) {
        return ventaDetalleRepository.findById(id);
    }

    public VentaDetalle save(VentaDetalle ventaDetalle) {
        return ventaDetalleRepository.save(ventaDetalle);
    }

    public void delete(Integer id) {
        ventaDetalleRepository.deleteById(id);
    }
}
