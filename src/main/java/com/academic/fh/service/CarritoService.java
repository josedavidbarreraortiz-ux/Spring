package com.academic.fh.service;

import com.academic.fh.model.Producto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarritoService {

    private static final String CARRITO_KEY = "carrito";
    private final ProductoService productoService;

    public CarritoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCarrito(HttpSession session) {
        List<Map<String, Object>> carrito = (List<Map<String, Object>>) session.getAttribute(CARRITO_KEY);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(CARRITO_KEY, carrito);
        }
        return carrito;
    }

    public void agregarItem(HttpSession session, Integer productoId, String nombre, Double precio, Integer cantidad) {
        List<Map<String, Object>> carrito = getCarrito(session);
        Map<String, Object> item = new HashMap<>();
        item.put("id", productoId);
        item.put("nombre", nombre);
        item.put("precio", precio);
        item.put("cantidad", cantidad);
        carrito.add(item);
    }

    public void agregarProducto(Long productoId, Integer cantidad, HttpSession session) {
        // Buscar el producto en la base de datos
        Producto producto = productoService.findById(productoId.intValue()).orElse(null);

        if (producto != null) {
            List<Map<String, Object>> carrito = getCarrito(session);

            // Verificar si el producto ya está en el carrito
            boolean encontrado = false;
            for (Map<String, Object> item : carrito) {
                if (item.get("id").equals(productoId.intValue())) {
                    // Si ya existe, actualizar la cantidad
                    Integer cantidadActual = (Integer) item.get("cantidad");
                    item.put("cantidad", cantidadActual + cantidad);
                    encontrado = true;
                    break;
                }
            }

            // Si no existe, agregarlo
            if (!encontrado) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", productoId.intValue());
                item.put("nombre", producto.getProductoNombre());
                item.put("precio",
                        producto.getProductoPrecioVenta() != null ? producto.getProductoPrecioVenta().doubleValue()
                                : 0.0);
                item.put("cantidad", cantidad);
                carrito.add(item);
            }
        }
    }

    public void eliminarItem(int index, HttpSession session) {
        List<Map<String, Object>> carrito = getCarrito(session);
        if (index >= 0 && index < carrito.size()) {
            carrito.remove(index);
        }
    }

    public void vaciarCarrito(HttpSession session) {
        session.removeAttribute(CARRITO_KEY);
    }
}
