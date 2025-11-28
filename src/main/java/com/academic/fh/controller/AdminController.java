package com.academic.fh.controller;

import com.academic.fh.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final VentaService ventaService;
    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ClienteService clienteService;
    private final InventarioService inventarioService;

    public AdminController(VentaService ventaService, ProductoService productoService,
            CategoriaService categoriaService, ClienteService clienteService, InventarioService inventarioService) {
        this.ventaService = ventaService;
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.clienteService = clienteService;
        this.inventarioService = inventarioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = new HashMap<>();

        // Populate stats
        stats.put("totalVentas", ventaService.findAll().size());
        stats.put("ventasMes", 0); // Placeholder
        stats.put("totalProductos", productoService.findAll().size());
        stats.put("categorias", categoriaService.findAll().size());
        stats.put("totalClientes", clienteService.findAll().size());

        // Stock crítico (inventarios con stock < 5)
        long stockCritico = inventarioService.findAll().stream()
                .filter(inv -> inv.getInventarioStockActual() < 5)
                .count();
        stats.put("stockCritico", stockCritico);

        model.addAttribute("stats", stats);

        // Ultimas ventas (lista vacía por ahora)
        model.addAttribute("ultimasVentas", new ArrayList<>());

        // Chart data (mapa con listas vacías)
        Map<String, Object> chart = new HashMap<>();
        chart.put("meses", new ArrayList<>());
        chart.put("ventas", new ArrayList<>());
        chart.put("movimientos", new ArrayList<>());
        model.addAttribute("chart", chart);

        return "admin/dashboard";
    }
}
