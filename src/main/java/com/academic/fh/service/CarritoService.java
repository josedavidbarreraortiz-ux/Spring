package com.academic.fh.service;

import com.academic.fh.model.Producto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CarritoService {

    private static final String CARRITO_SESSION_KEY = "carrito";
    private final ProductoService productoService;

    public CarritoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Obtener el carrito de la sesión
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCarrito(HttpSession session) {
        List<Map<String, Object>> carrito = (List<Map<String, Object>>) session.getAttribute(CARRITO_SESSION_KEY);

        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(CARRITO_SESSION_KEY, carrito);
        }

        return carrito;
    }

    /**
     * Agregar producto al carrito
     */
    public void agregarProducto(Long productoId, Integer cantidad, HttpSession session) {
        Producto producto = productoService.getProductoOrThrow(productoId);

        List<Map<String, Object>> carrito = getCarrito(session);

        // Crear item del carrito
        Map<String, Object> item = new HashMap<>();
        item.put("id", producto.getProductoId());
        item.put("nombre", producto.getProductoNombre());
        item.put("precio", producto.getProductoPrecioVenta());
        item.put("cantidad", cantidad);

        carrito.add(item);
        session.setAttribute(CARRITO_SESSION_KEY, carrito);
    }

    /**
     * Eliminar item del carrito por índice
     */
    public void eliminarItem(int index, HttpSession session) {
        List<Map<String, Object>> carrito = getCarrito(session);

        if (index >= 0 && index < carrito.size()) {
            carrito.remove(index);
            session.setAttribute(CARRITO_SESSION_KEY, carrito);
        }
    }

    /**
     * Vaciar el carrito completamente
     */
    public void vaciarCarrito(HttpSession session) {
        session.removeAttribute(CARRITO_SESSION_KEY);
    }

    /**
     * Calcular el total del carrito
     */
    public BigDecimal calcularTotal(List<Map<String, Object>> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return carrito.stream()
                .map(item -> {
                    Integer cantidad = (Integer) item.get("cantidad");
                    BigDecimal precio = (BigDecimal) item.get("precio");
                    return precio.multiply(BigDecimal.valueOf(cantidad));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verificar si el carrito está vacío
     */
    public boolean isCarritoVacio(HttpSession session) {
        List<Map<String, Object>> carrito = getCarrito(session);
        return carrito.isEmpty();
    }

    /**
     * Obtener cantidad de items en el carrito
     */
    public int getCantidadItems(HttpSession session) {
        List<Map<String, Object>> carrito = getCarrito(session);
        return carrito.size();
    }
}
