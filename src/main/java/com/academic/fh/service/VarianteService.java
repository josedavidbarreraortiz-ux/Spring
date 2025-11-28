package com.academic.fh.service;

import com.academic.fh.model.ProductoVariante;
import com.academic.fh.repository.ProductoVarianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VarianteService {

    private final ProductoVarianteRepository productoVarianteRepository;

    public VarianteService(ProductoVarianteRepository productoVarianteRepository) {
        this.productoVarianteRepository = productoVarianteRepository;
    }

    public List<ProductoVariante> findAll() {
        return productoVarianteRepository.findAll();
    }

    public Optional<ProductoVariante> findById(Integer id) {
        return productoVarianteRepository.findById(id);
    }

    public ProductoVariante save(ProductoVariante variante) {
        return productoVarianteRepository.save(variante);
    }

    public void delete(Integer id) {
        productoVarianteRepository.deleteById(id);
    }
}
