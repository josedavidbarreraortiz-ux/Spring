package com.academic.fh.controller;

import com.academic.fh.model.Venta;
import com.academic.fh.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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

        List<Venta> todasLasVentas = ventaService.findAll();
        LocalDate hoy = LocalDate.now();
        YearMonth mesActual = YearMonth.from(hoy);

        // Contar ventas del mes actual
        long ventasMesActual = todasLasVentas.stream()
                .filter(v -> v.getVentaFecha() != null)
                .filter(v -> {
                    YearMonth ventaMes = YearMonth.from(v.getVentaFecha());
                    return ventaMes.equals(mesActual);
                })
                .count();

        // Populate stats
        stats.put("totalVentas", todasLasVentas.size());
        stats.put("ventasMes", ventasMesActual);
        stats.put("totalProductos", productoService.findAll().size());
        stats.put("categorias", categoriaService.findAll().size());
        stats.put("totalClientes", clienteService.findAll().size());

        // Stock crítico (inventarios con stock < 5)
        long stockCritico = inventarioService.findAll().stream()
                .filter(inv -> inv.getInventarioStockActual() < 5)
                .count();
        stats.put("stockCritico", stockCritico);

        model.addAttribute("stats", stats);

        // Últimas 5 ventas
        List<Map<String, Object>> ultimasVentas = todasLasVentas.stream()
                .sorted((v1, v2) -> {
                    // Ordenar por fecha descendente (más reciente primero)
                    if (v1.getVentaFecha() == null)
                        return 1;
                    if (v2.getVentaFecha() == null)
                        return -1;
                    int fechaComp = v2.getVentaFecha().compareTo(v1.getVentaFecha());
                    if (fechaComp != 0)
                        return fechaComp;
                    // Si misma fecha, ordenar por hora
                    if (v1.getVentaHora() == null)
                        return 1;
                    if (v2.getVentaHora() == null)
                        return -1;
                    return v2.getVentaHora().compareTo(v1.getVentaHora());
                })
                .limit(5)
                .map(v -> {
                    Map<String, Object> ventaMap = new HashMap<>();
                    ventaMap.put("codigo", v.getVentaId());
                    ventaMap.put("clienteNombre", v.getUser() != null ? v.getUser().getName()
                            : (v.getCliente() != null ? v.getCliente().getClienteNombre() : "N/A"));
                    ventaMap.put("total", formatCOP(v.getVentaTotal()));
                    ventaMap.put("fecha", v.getVentaFecha() != null ? v.getVentaFecha().toString() : "N/A");
                    ventaMap.put("metodo", v.getMetodoPago() != null ? v.getMetodoPago().getMetodoPagoNombre() : "N/A");
                    return ventaMap;
                })
                .collect(Collectors.toList());

        model.addAttribute("ultimasVentas", ultimasVentas);

        // Chart data - últimos 6 meses
        Map<String, Object> chart = generarDatosGrafico(todasLasVentas);
        model.addAttribute("chart", chart);

        return "admin/dashboard";
    }

    private Map<String, Object> generarDatosGrafico(List<Venta> ventas) {
        Map<String, Object> chart = new HashMap<>();
        List<String> meses = new ArrayList<>();
        List<Integer> ventasPorMes = new ArrayList<>();
        List<Integer> movimientosPorMes = new ArrayList<>();

        YearMonth mesActual = YearMonth.now();

        // Generar últimos 6 meses
        for (int i = 5; i >= 0; i--) {
            YearMonth mes = mesActual.minusMonths(i);

            // Nombre del mes en español
            String nombreMes = mes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            meses.add(nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1));

            // Contar ventas de ese mes
            long ventasDelMes = ventas.stream()
                    .filter(v -> v.getVentaFecha() != null)
                    .filter(v -> YearMonth.from(v.getVentaFecha()).equals(mes))
                    .count();

            ventasPorMes.add((int) ventasDelMes);

            // Movimientos = ventas * 2 (ejemplo: cada venta genera entrada y salida)
            movimientosPorMes.add((int) ventasDelMes * 2);
        }

        chart.put("meses", meses);
        chart.put("ventas", ventasPorMes);
        chart.put("movimientos", movimientosPorMes);

        return chart;
    }

    // Formatear precio en pesos colombianos (ej: $1.800.000)
    private String formatCOP(java.math.BigDecimal value) {
        if (value == null)
            return "$0";
        java.text.NumberFormat nf = java.text.NumberFormat.getIntegerInstance(new java.util.Locale("es", "CO"));
        return "$" + nf.format(value.longValue());
    }
}
