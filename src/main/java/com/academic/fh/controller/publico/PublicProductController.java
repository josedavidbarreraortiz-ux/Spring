package com.academic.fh.controller.publico;

import com.academic.fh.model.Producto;
import com.academic.fh.service.CategoriaService;
import com.academic.fh.service.InventarioService;
import com.academic.fh.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/productos")
public class PublicProductController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final InventarioService inventarioService;

    public PublicProductController(ProductoService productoService, CategoriaService categoriaService,
            InventarioService inventarioService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public String listado(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            Model model) {

        var productos = productoService.buscarConFiltros(nombre, categoriaId, precioMin, precioMax);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("stockMap", buildStockMap(productos));

        return "products";
    }

    @GetMapping("/categoria/{id}")
    public String productosPorCategoria(@PathVariable Integer id, Model model) {
        var productos = productoService.buscarConFiltros(null, id, null, null);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("categoriaActual", categoriaService.findById(id).orElse(null));
        model.addAttribute("stockMap", buildStockMap(productos));

        return "products";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {

        var producto = productoService.findById(id).orElse(null);

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());

        // Obtener stock del producto
        Integer stock = 0;
        if (producto != null) {
            var inventarioOpt = inventarioService.findByProductoId(producto.getProductoId());
            if (inventarioOpt.isPresent()) {
                stock = inventarioOpt.get().getInventarioStockActual();
                if (stock == null)
                    stock = 0;
            }
        }
        model.addAttribute("stockProducto", stock);

        return "product-detail";
    }

    /**
     * Construye un mapa de productoId -> stock para mostrar disponibilidad
     */
    private Map<Integer, Integer> buildStockMap(List<Producto> productos) {
        Map<Integer, Integer> stockMap = new HashMap<>();
        for (Producto p : productos) {
            var inventarioOpt = inventarioService.findByProductoId(p.getProductoId());
            Integer stock = 0;
            if (inventarioOpt.isPresent()) {
                stock = inventarioOpt.get().getInventarioStockActual();
                if (stock == null)
                    stock = 0;
            }
            stockMap.put(p.getProductoId(), stock);
        }
        return stockMap;
    }
}
