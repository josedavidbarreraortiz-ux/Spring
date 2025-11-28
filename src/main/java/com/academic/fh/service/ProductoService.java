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
    private final com.academic.fh.repository.InventarioRepository inventarioRepository;
    private final com.academic.fh.repository.MovimientoInventarioRepository movimientoInventarioRepository;
    private final com.academic.fh.repository.ProductoAtributoRepository productoAtributoRepository;
    private final com.academic.fh.repository.ProductoVarianteRepository productoVarianteRepository;
    private final com.academic.fh.repository.ProductoGarantiaRepository productoGarantiaRepository;
    private final com.academic.fh.repository.ProductoCategoriaRepository productoCategoriaRepository;

    public ProductoService(ProductoRepository productoRepository,
            com.academic.fh.repository.InventarioRepository inventarioRepository,
            com.academic.fh.repository.MovimientoInventarioRepository movimientoInventarioRepository,
            com.academic.fh.repository.ProductoAtributoRepository productoAtributoRepository,
            com.academic.fh.repository.ProductoVarianteRepository productoVarianteRepository,
            com.academic.fh.repository.ProductoGarantiaRepository productoGarantiaRepository,
            com.academic.fh.repository.ProductoCategoriaRepository productoCategoriaRepository) {
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.productoAtributoRepository = productoAtributoRepository;
        this.productoVarianteRepository = productoVarianteRepository;
        this.productoGarantiaRepository = productoGarantiaRepository;
        this.productoCategoriaRepository = productoCategoriaRepository;
    }

    /**
     * Obtener todos los productos
     */
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    /**
     * Buscar producto por ID
     */
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Guardar o actualizar producto
     */
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Eliminar producto por ID
     */
    public void delete(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();

            // 1. Eliminar inventarios asociados
            if (producto.getInventarios() != null && !producto.getInventarios().isEmpty()) {
                inventarioRepository.deleteAll(producto.getInventarios());
            }

            // 2. Eliminar movimientos de inventario asociados
            if (producto.getMovimientosInventario() != null && !producto.getMovimientosInventario().isEmpty()) {
                movimientoInventarioRepository.deleteAll(producto.getMovimientosInventario());
            }

            // 3. Eliminar atributos asociados
            if (producto.getAtributos() != null && !producto.getAtributos().isEmpty()) {
                productoAtributoRepository.deleteAll(producto.getAtributos());
            }

            // 4. Eliminar variantes asociadas
            if (producto.getVariantes() != null && !producto.getVariantes().isEmpty()) {
                productoVarianteRepository.deleteAll(producto.getVariantes());
            }

            // 5. Eliminar garantías asociadas
            if (producto.getGarantias() != null && !producto.getGarantias().isEmpty()) {
                productoGarantiaRepository.deleteAll(producto.getGarantias());
            }

            // 6. Eliminar categorías secundarias asociadas
            if (producto.getCategoriasSecundarias() != null && !producto.getCategoriasSecundarias().isEmpty()) {
                productoCategoriaRepository.deleteAll(producto.getCategoriasSecundarias());
            }

            // Nota: Si hay ventas (VentaDetalle), esto aún podría fallar.
            // Por ahora asumimos que se quieren borrar productos sin ventas o que la BD lo
            // impide.

            productoRepository.deleteById(id);
        }
    }

    /**
     * Verificar si un producto existe
     */
    public boolean existsById(Long id) {
        return productoRepository.existsById(id);
    }

    /**
     * Obtener producto o lanzar excepción
     */
    public Producto getProductoOrThrow(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    /**
     * Buscar productos con filtros multicriterio
     */
    public List<Producto> buscarConFiltros(String nombre, Long categoriaId, Double precioMin, Double precioMax) {
        List<Producto> productos = productoRepository.findAll();

        // Filtrar por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            String nombreLower = nombre.toLowerCase();
            productos = productos.stream()
                    .filter(p -> p.getProductoNombre() != null &&
                            p.getProductoNombre().toLowerCase().contains(nombreLower))
                    .toList();
        }

        // Filtrar por categoría
        if (categoriaId != null) {
            productos = productos.stream()
                    .filter(p -> p.getCategoriaPrincipal() != null &&
                            p.getCategoriaPrincipal().getCategoriaId().equals(categoriaId))
                    .toList();
        }

        // Filtrar por precio mínimo
        if (precioMin != null) {
            productos = productos.stream()
                    .filter(p -> p.getProductoPrecioVenta() != null &&
                            p.getProductoPrecioVenta().doubleValue() >= precioMin)
                    .toList();
        }

        // Filtrar por precio máximo
        if (precioMax != null) {
            productos = productos.stream()
                    .filter(p -> p.getProductoPrecioVenta() != null &&
                            p.getProductoPrecioVenta().doubleValue() <= precioMax)
                    .toList();
        }

        return productos;
    }
}
