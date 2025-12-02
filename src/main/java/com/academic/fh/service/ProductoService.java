package com.academic.fh.service;

import com.academic.fh.model.Producto;
import com.academic.fh.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(Integer id) {
        // First delete related inventario records
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Delete inventario records for this producto
        if (producto.getInventarios() != null) {
            producto.getInventarios().clear();
            productoRepository.save(producto);
        }

        productoRepository.deleteById(id);
    }

    public Producto getProductoOrThrow(Long id) {
        return productoRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<Producto> buscarConFiltros(String nombre, Integer categoriaId, Double precioMin, Double precioMax) {
        return productoRepository.findAll().stream()
                .filter(p -> nombre == null || p.getProductoNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(p -> categoriaId == null || (p.getCategoriaPrincipal() != null
                        && p.getCategoriaPrincipal().getCategoriaId().equals(categoriaId)))
                .filter(p -> precioMin == null || p.getProductoPrecioVenta().doubleValue() >= precioMin)
                .filter(p -> precioMax == null || p.getProductoPrecioVenta().doubleValue() <= precioMax)
                .toList();
    }
}
