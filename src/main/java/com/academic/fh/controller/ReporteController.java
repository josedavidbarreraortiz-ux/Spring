package com.academic.fh.controller;

import com.academic.fh.model.Producto;
import com.academic.fh.model.Inventario;
import com.academic.fh.model.User;
import com.academic.fh.model.Cliente;
import com.academic.fh.model.Venta;
import com.academic.fh.model.Categoria;
import com.academic.fh.model.MovimientoInventario;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.InventarioService;
import com.academic.fh.service.UserService;
import com.academic.fh.service.ClienteService;
import com.academic.fh.service.CategoriaService;
import com.academic.fh.service.VentaService;
import com.academic.fh.service.MetodoPagoService;
import com.academic.fh.service.MovimientoInventarioService;
import com.academic.fh.service.PdfReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/reportes")
public class ReporteController {

    private final ProductoService productoService;
    private final InventarioService inventarioService;
    private final UserService userService;
    private final ClienteService clienteService;
    private final CategoriaService categoriaService;
    private final VentaService ventaService;
    private final MetodoPagoService metodoPagoService;
    private final MovimientoInventarioService movimientoInventarioService;
    private final PdfReportService pdfReportService;

    public ReporteController(ProductoService productoService,
            InventarioService inventarioService,
            UserService userService,
            ClienteService clienteService,
            CategoriaService categoriaService,
            VentaService ventaService,
            MetodoPagoService metodoPagoService,
            MovimientoInventarioService movimientoInventarioService,
            PdfReportService pdfReportService) {
        this.productoService = productoService;
        this.inventarioService = inventarioService;
        this.userService = userService;
        this.clienteService = clienteService;
        this.categoriaService = categoriaService;
        this.ventaService = ventaService;
        this.metodoPagoService = metodoPagoService;
        this.movimientoInventarioService = movimientoInventarioService;
        this.pdfReportService = pdfReportService;
    }

    // Obtener mapa de stocks
    private Map<Integer, Integer> getStockMap() {
        Map<Integer, Integer> stockMap = new HashMap<>();
        for (Inventario inv : inventarioService.findAll()) {
            if (inv.getProducto() != null) {
                stockMap.put(inv.getProducto().getProductoId(), inv.getInventarioStockActual());
            }
        }
        return stockMap;
    }

    // Formatear precio en pesos colombianos (ej: $1.800.000)
    private String formatCOP(BigDecimal value) {
        if (value == null)
            return "$0";
        java.text.NumberFormat nf = java.text.NumberFormat.getIntegerInstance(new java.util.Locale("es", "CO"));
        return "$" + nf.format(value.longValue());
    }

    // ==================== PRODUCTOS ====================

    @GetMapping("/productos")
    public String formReporteProductos(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("marcas", productoService.findDistinctMarcas());
        return "admin/reportes/productos";
    }

    @GetMapping("/productos/pdf")
    public ResponseEntity<byte[]> generarReporteProductos(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false, defaultValue = "nombre") String ordenarPor,
            @RequestParam(required = false, defaultValue = "asc") String direccion) {

        Map<Integer, Integer> stockMap = getStockMap();

        List<Producto> productos = productoService.findAll().stream()
                .filter(p -> id == null || p.getProductoId().equals(id))
                .filter(p -> nombre == null || nombre.isEmpty() ||
                        (p.getProductoNombre() != null
                                && p.getProductoNombre().toLowerCase().contains(nombre.toLowerCase())))
                .filter(p -> marca == null || marca.isEmpty() ||
                        (p.getProductoMarca() != null && p.getProductoMarca().equalsIgnoreCase(marca)))
                .filter(p -> categoriaId == null ||
                        (p.getCategoriaPrincipal() != null
                                && p.getCategoriaPrincipal().getCategoriaId().equals(categoriaId)))
                .filter(p -> estado == null || estado.isEmpty() ||
                        (p.getProductoEstado() != null && p.getProductoEstado().equalsIgnoreCase(estado)))
                .filter(p -> {
                    if (stock == null || stock.isEmpty())
                        return true;
                    Integer stockActual = stockMap.get(p.getProductoId());
                    if ("Agotado".equalsIgnoreCase(stock)) {
                        return stockActual != null && stockActual == 0;
                    }
                    if ("ConStock".equalsIgnoreCase(stock)) {
                        return stockActual != null && stockActual > 0;
                    }
                    return true;
                })
                .filter(p -> precioMin == null ||
                        (p.getProductoPrecioVenta() != null && p.getProductoPrecioVenta().compareTo(precioMin) >= 0))
                .filter(p -> precioMax == null ||
                        (p.getProductoPrecioVenta() != null && p.getProductoPrecioVenta().compareTo(precioMax) <= 0))
                .collect(Collectors.toList());

        Comparator<Producto> comparator = switch (ordenarPor) {
            case "id" -> Comparator.comparing(Producto::getProductoId, Comparator.nullsLast(Comparator.naturalOrder()));
            case "precio" ->
                Comparator.comparing(Producto::getProductoPrecioVenta, Comparator.nullsLast(Comparator.naturalOrder()));
            case "stock" ->
                Comparator.comparing(p -> stockMap.getOrDefault(p.getProductoId(), 0), Comparator.naturalOrder());
            case "marca" -> Comparator.comparing(p -> p.getProductoMarca() != null ? p.getProductoMarca() : "",
                    Comparator.naturalOrder());
            default -> Comparator.comparing(p -> p.getProductoNombre() != null ? p.getProductoNombre() : "",
                    Comparator.naturalOrder());
        };

        if ("desc".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }
        productos.sort(comparator);

        String[] headers = { "ID", "Nombre", "Marca", "Categoría", "Precio", "Stock", "Estado" };
        List<String[]> datos = productos.stream()
                .map(p -> {
                    Integer stockActual = stockMap.get(p.getProductoId());
                    String stockStr = stockActual != null ? String.valueOf(stockActual) : "0";
                    String estadoStr = p.getProductoEstado() != null ? p.getProductoEstado() : "";
                    return new String[] {
                            String.valueOf(p.getProductoId()),
                            p.getProductoNombre() != null ? p.getProductoNombre() : "",
                            p.getProductoMarca() != null ? p.getProductoMarca() : "",
                            p.getCategoriaPrincipal() != null ? p.getCategoriaPrincipal().getCategoriaNombre() : "",
                            formatCOP(p.getProductoPrecioVenta()),
                            stockStr,
                            estadoStr
                    };
                })
                .collect(Collectors.toList());

        String subtitulo = generarSubtituloProductos(nombre, marca, categoriaId, estado, stock, precioMin, precioMax);
        byte[] pdf = pdfReportService.generarReporte("Reporte de Productos", subtitulo, headers, datos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_productos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private String generarSubtituloProductos(String nombre, String marca, Integer categoriaId,
            String estado, String stock, BigDecimal precioMin, BigDecimal precioMax) {
        List<String> filtros = new ArrayList<>();
        if (nombre != null && !nombre.isEmpty())
            filtros.add("Nombre: " + nombre);
        if (marca != null && !marca.isEmpty())
            filtros.add("Marca: " + marca);
        if (categoriaId != null)
            filtros.add("Categoría ID: " + categoriaId);
        if (estado != null && !estado.isEmpty())
            filtros.add("Estado: " + estado);
        if (stock != null && !stock.isEmpty()) {
            if ("Agotado".equalsIgnoreCase(stock)) {
                filtros.add("Stock: Agotado (0)");
            } else if ("ConStock".equalsIgnoreCase(stock)) {
                filtros.add("Stock: Disponible (>0)");
            }
        }
        if (precioMin != null)
            filtros.add("Precio mín: $" + precioMin);
        if (precioMax != null)
            filtros.add("Precio máx: $" + precioMax);

        return filtros.isEmpty() ? "Todos los productos" : "Filtros: " + String.join(" | ", filtros);
    }

    // ==================== CATEGORÍAS ====================

    @GetMapping("/categorias")
    public String formReporteCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "admin/reportes/categorias";
    }

    @GetMapping("/categorias/pdf")
    public ResponseEntity<byte[]> generarReporteCategorias(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false, defaultValue = "categoria") String ordenarPor,
            @RequestParam(required = false, defaultValue = "asc") String direccion) {

        Map<Integer, Integer> stockMap = getStockMap();

        List<Producto> productos = productoService.findAll().stream()
                .filter(p -> categoriaId == null ||
                        (p.getCategoriaPrincipal() != null
                                && p.getCategoriaPrincipal().getCategoriaId().equals(categoriaId)))
                .filter(p -> nombre == null || nombre.isEmpty() ||
                        (p.getProductoNombre() != null
                                && p.getProductoNombre().toLowerCase().contains(nombre.toLowerCase())))
                .filter(p -> {
                    if (stock == null || stock.isEmpty())
                        return true;
                    Integer stockActual = stockMap.get(p.getProductoId());
                    if ("Agotado".equalsIgnoreCase(stock)) {
                        return stockActual != null && stockActual == 0;
                    }
                    if ("ConStock".equalsIgnoreCase(stock)) {
                        return stockActual != null && stockActual > 0;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        Comparator<Producto> comparator = switch (ordenarPor) {
            case "nombre" -> Comparator.comparing(p -> p.getProductoNombre() != null ? p.getProductoNombre() : "",
                    Comparator.naturalOrder());
            case "precio" ->
                Comparator.comparing(Producto::getProductoPrecioVenta, Comparator.nullsLast(Comparator.naturalOrder()));
            case "stock" ->
                Comparator.comparing(p -> stockMap.getOrDefault(p.getProductoId(), 0), Comparator.naturalOrder());
            default -> Comparator.comparing(
                    (Producto p) -> p.getCategoriaPrincipal() != null ? p.getCategoriaPrincipal().getCategoriaNombre()
                            : "",
                    Comparator.naturalOrder()).thenComparing(
                            p -> p.getProductoNombre() != null ? p.getProductoNombre() : "", Comparator.naturalOrder());
        };

        if ("desc".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }
        productos.sort(comparator);

        String[] headers = { "Categoría", "ID Producto", "Producto", "Marca", "Precio", "Stock" };
        List<String[]> datos = productos.stream()
                .map(p -> {
                    Integer stockActual = stockMap.get(p.getProductoId());
                    return new String[] {
                            p.getCategoriaPrincipal() != null ? p.getCategoriaPrincipal().getCategoriaNombre()
                                    : "Sin categoría",
                            String.valueOf(p.getProductoId()),
                            p.getProductoNombre() != null ? p.getProductoNombre() : "",
                            p.getProductoMarca() != null ? p.getProductoMarca() : "",
                            formatCOP(p.getProductoPrecioVenta()),
                            stockActual != null ? String.valueOf(stockActual) : "0"
                    };
                })
                .collect(Collectors.toList());

        String subtitulo = generarSubtituloCategorias(categoriaId, nombre, stock);
        byte[] pdf = pdfReportService.generarReporte("Reporte de Productos por Categoría", subtitulo, headers, datos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_categorias.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private String generarSubtituloCategorias(Integer categoriaId, String nombre, String stock) {
        List<String> filtros = new ArrayList<>();
        if (categoriaId != null) {
            Categoria cat = categoriaService.findById(categoriaId).orElse(null);
            if (cat != null)
                filtros.add("Categoría: " + cat.getCategoriaNombre());
        }
        if (nombre != null && !nombre.isEmpty())
            filtros.add("Producto: " + nombre);
        if (stock != null && !stock.isEmpty()) {
            filtros.add("Stock: " + ("Agotado".equalsIgnoreCase(stock) ? "Agotado" : "Disponible"));
        }
        return filtros.isEmpty() ? "Todas las categorías" : "Filtros: " + String.join(" | ", filtros);
    }

    // ==================== VENTAS ====================

    @GetMapping("/ventas")
    public String formReporteVentas(Model model) {
        model.addAttribute("usuarios", userService.findAll());
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("metodosPago", metodoPagoService.findAll());
        return "admin/reportes/ventas";
    }

    @GetMapping("/ventas/pdf")
    public ResponseEntity<byte[]> generarReporteVentas(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(required = false) Integer metodoPagoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) BigDecimal totalMin,
            @RequestParam(required = false) BigDecimal totalMax,
            @RequestParam(required = false, defaultValue = "fecha") String ordenarPor,
            @RequestParam(required = false, defaultValue = "desc") String direccion) {

        List<Venta> ventas = ventaService.findAll().stream()
                .filter(v -> id == null || v.getVentaId().equals(id))
                .filter(v -> userId == null || (v.getUser() != null && v.getUser().getId().equals(userId)))
                .filter(v -> clienteId == null
                        || (v.getCliente() != null && v.getCliente().getClienteId().equals(clienteId)))
                .filter(v -> metodoPagoId == null
                        || (v.getMetodoPago() != null && v.getMetodoPago().getMetodoPagoId().equals(metodoPagoId)))
                .filter(v -> fechaDesde == null
                        || (v.getVentaFecha() != null && !v.getVentaFecha().isBefore(fechaDesde)))
                .filter(v -> fechaHasta == null
                        || (v.getVentaFecha() != null && !v.getVentaFecha().isAfter(fechaHasta)))
                .filter(v -> totalMin == null
                        || (v.getVentaTotal() != null && v.getVentaTotal().compareTo(totalMin) >= 0))
                .filter(v -> totalMax == null
                        || (v.getVentaTotal() != null && v.getVentaTotal().compareTo(totalMax) <= 0))
                .collect(Collectors.toList());

        Comparator<Venta> comparator = switch (ordenarPor) {
            case "id" -> Comparator.comparing(Venta::getVentaId, Comparator.nullsLast(Comparator.naturalOrder()));
            case "total" -> Comparator.comparing(Venta::getVentaTotal, Comparator.nullsLast(Comparator.naturalOrder()));
            case "cliente" -> Comparator.comparing(
                    v -> v.getCliente() != null && v.getCliente().getClienteNombre() != null
                            ? v.getCliente().getClienteNombre()
                            : "",
                    Comparator.naturalOrder());
            default -> Comparator.comparing(Venta::getVentaFecha, Comparator.nullsLast(Comparator.naturalOrder()));
        };

        if ("desc".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }
        ventas.sort(comparator);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String[] headers = { "ID", "Fecha", "Cliente", "Vendedor", "Método Pago", "Total" };
        List<String[]> datos = ventas.stream()
                .map(v -> new String[] {
                        String.valueOf(v.getVentaId()),
                        v.getVentaFecha() != null ? v.getVentaFecha().format(dateFormatter) : "",
                        v.getCliente() != null && v.getCliente().getClienteNombre() != null
                                ? v.getCliente().getClienteNombre()
                                : "",
                        v.getUser() != null && v.getUser().getName() != null ? v.getUser().getName() : "",
                        v.getMetodoPago() != null && v.getMetodoPago().getMetodoPagoNombre() != null
                                ? v.getMetodoPago().getMetodoPagoNombre()
                                : "",
                        formatCOP(v.getVentaTotal())
                })
                .collect(Collectors.toList());

        // Calcular total de ventas
        BigDecimal totalVentas = ventas.stream()
                .map(Venta::getVentaTotal)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String subtitulo = generarSubtituloVentas(userId, clienteId, fechaDesde, fechaHasta, totalMin, totalMax);
        subtitulo += " | Total: " + formatCOP(totalVentas);

        byte[] pdf = pdfReportService.generarReporte("Reporte de Ventas", subtitulo, headers, datos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_ventas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private String generarSubtituloVentas(Long userId, Integer clienteId, LocalDate fechaDesde,
            LocalDate fechaHasta, BigDecimal totalMin, BigDecimal totalMax) {
        List<String> filtros = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (userId != null) {
            User user = userService.findById(userId).orElse(null);
            if (user != null)
                filtros.add("Vendedor: " + user.getName());
        }
        if (clienteId != null) {
            Cliente cliente = clienteService.findById(clienteId).orElse(null);
            if (cliente != null)
                filtros.add("Cliente: " + cliente.getClienteNombre());
        }
        if (fechaDesde != null)
            filtros.add("Desde: " + fechaDesde.format(dateFormatter));
        if (fechaHasta != null)
            filtros.add("Hasta: " + fechaHasta.format(dateFormatter));
        if (totalMin != null)
            filtros.add("Total mín: $" + totalMin);
        if (totalMax != null)
            filtros.add("Total máx: $" + totalMax);

        return filtros.isEmpty() ? "Todas las ventas" : "Filtros: " + String.join(" | ", filtros);
    }

    // ==================== USUARIOS ====================

    @GetMapping("/usuarios")
    public String formReporteUsuarios(Model model) {
        return "admin/reportes/usuarios";
    }

    @GetMapping("/usuarios/pdf")
    public ResponseEntity<byte[]> generarReporteUsuarios(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(required = false, defaultValue = "nombre") String ordenarPor,
            @RequestParam(required = false, defaultValue = "asc") String direccion) {

        List<User> usuarios = userService.findAll().stream()
                .filter(u -> id == null || u.getId().equals(id))
                .filter(u -> nombre == null || nombre.isEmpty() ||
                        (u.getName() != null && u.getName().toLowerCase().contains(nombre.toLowerCase())))
                .filter(u -> email == null || email.isEmpty() ||
                        (u.getEmail() != null && u.getEmail().toLowerCase().contains(email.toLowerCase())))
                .filter(u -> role == null || role.isEmpty() ||
                        (u.getRole() != null && u.getRole().equalsIgnoreCase(role)))
                .collect(Collectors.toList());

        Comparator<User> comparator = switch (ordenarPor) {
            case "id" -> Comparator.comparing(User::getId, Comparator.nullsLast(Comparator.naturalOrder()));
            case "email" ->
                Comparator.comparing(u -> u.getEmail() != null ? u.getEmail() : "", Comparator.naturalOrder());
            case "role" -> Comparator.comparing(u -> u.getRole() != null ? u.getRole() : "", Comparator.naturalOrder());
            default -> Comparator.comparing(u -> u.getName() != null ? u.getName() : "", Comparator.naturalOrder());
        };

        if ("desc".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }
        usuarios.sort(comparator);

        String[] headers = { "ID", "Nombre", "Email", "Rol" };
        List<String[]> datos = usuarios.stream()
                .map(u -> new String[] {
                        String.valueOf(u.getId()),
                        u.getName() != null ? u.getName() : "",
                        u.getEmail() != null ? u.getEmail() : "",
                        u.getRole() != null ? u.getRole() : ""
                })
                .collect(Collectors.toList());

        String subtitulo = generarSubtituloUsuarios(nombre, email, role);
        byte[] pdf = pdfReportService.generarReporte("Reporte de Usuarios", subtitulo, headers, datos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_usuarios.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private String generarSubtituloUsuarios(String nombre, String email, String role) {
        List<String> filtros = new ArrayList<>();
        if (nombre != null && !nombre.isEmpty())
            filtros.add("Nombre: " + nombre);
        if (email != null && !email.isEmpty())
            filtros.add("Email: " + email);
        if (role != null && !role.isEmpty())
            filtros.add("Rol: " + role);

        return filtros.isEmpty() ? "Todos los usuarios" : "Filtros: " + String.join(" | ", filtros);
    }

    // ==================== CLIENTES ====================

    @GetMapping("/clientes")
    public String formReporteClientes(Model model) {
        return "admin/reportes/clientes";
    }

    @GetMapping("/clientes/pdf")
    public ResponseEntity<byte[]> generarReporteClientes(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String documento,
            @RequestParam(required = false, defaultValue = "nombre") String ordenarPor,
            @RequestParam(required = false, defaultValue = "asc") String direccion) {

        List<Cliente> clientes = clienteService.findAll().stream()
                .filter(c -> id == null || c.getClienteId().equals(id))
                .filter(c -> nombre == null || nombre.isEmpty() ||
                        (c.getClienteNombre() != null
                                && c.getClienteNombre().toLowerCase().contains(nombre.toLowerCase())))
                .filter(c -> email == null || email.isEmpty() ||
                        (c.getClienteEmail() != null
                                && c.getClienteEmail().toLowerCase().contains(email.toLowerCase())))
                .filter(c -> documento == null || documento.isEmpty() ||
                        (c.getClienteNumeroDocumento() != null && c.getClienteNumeroDocumento().contains(documento)))
                .collect(Collectors.toList());

        Comparator<Cliente> comparator = switch (ordenarPor) {
            case "id" -> Comparator.comparing(Cliente::getClienteId, Comparator.nullsLast(Comparator.naturalOrder()));
            case "email" -> Comparator.comparing(c -> c.getClienteEmail() != null ? c.getClienteEmail() : "",
                    Comparator.naturalOrder());
            default -> Comparator.comparing(c -> c.getClienteNombre() != null ? c.getClienteNombre() : "",
                    Comparator.naturalOrder());
        };

        if ("desc".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }
        clientes.sort(comparator);

        String[] headers = { "ID", "Nombre", "Email", "Teléfono", "Documento" };
        List<String[]> datos = clientes.stream()
                .map(c -> new String[] {
                        String.valueOf(c.getClienteId()),
                        c.getClienteNombre() != null ? c.getClienteNombre() : "",
                        c.getClienteEmail() != null ? c.getClienteEmail() : "",
                        c.getClienteTelefono() != null ? c.getClienteTelefono() : "",
                        c.getClienteNumeroDocumento() != null ? c.getClienteNumeroDocumento() : ""
                })
                .collect(Collectors.toList());

        String subtitulo = generarSubtituloClientes(nombre, email, documento);
        byte[] pdf = pdfReportService.generarReporte("Reporte de Clientes", subtitulo, headers, datos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_clientes.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private String generarSubtituloClientes(String nombre, String email, String documento) {
        List<String> filtros = new ArrayList<>();
        if (nombre != null && !nombre.isEmpty())
            filtros.add("Nombre: " + nombre);
        if (email != null && !email.isEmpty())
            filtros.add("Email: " + email);
        if (documento != null && !documento.isEmpty())
            filtros.add("Documento: " + documento);

        return filtros.isEmpty() ? "Todos los clientes" : "Filtros: " + String.join(" | ", filtros);
    }

    // ==================== MOVIMIENTOS DE INVENTARIO ====================

    @GetMapping("/movimientos")
    public String formReporteMovimientos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("totalMovimientos", movimientoInventarioService.countTotal());
        model.addAttribute("totalEntradas", movimientoInventarioService.countByTipo("ENTRADA"));
        model.addAttribute("totalSalidas", movimientoInventarioService.countByTipo("SALIDA"));
        model.addAttribute("totalAjustes", movimientoInventarioService.countByTipo("AJUSTE"));
        return "admin/reportes/movimientos";
    }

    @GetMapping("/movimientos/pdf")
    public ResponseEntity<byte[]> generarReporteMovimientos(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Integer productoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false, defaultValue = "fecha") String ordenarPor,
            @RequestParam(required = false, defaultValue = "desc") String direccion) {

        List<MovimientoInventario> movimientos = movimientoInventarioService.findByFiltros(
                tipo, productoId, fechaInicio, fechaFin);

        Comparator<MovimientoInventario> comparator = switch (ordenarPor) {
            case "id" -> Comparator.comparing(MovimientoInventario::getMovimientoId,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case "producto" -> Comparator.comparing(
                    m -> m.getProducto() != null ? m.getProducto().getProductoNombre() : "",
                    Comparator.naturalOrder());
            case "tipo" -> Comparator.comparing(
                    m -> m.getMovimientoTipo() != null ? m.getMovimientoTipo() : "",
                    Comparator.naturalOrder());
            case "cantidad" -> Comparator.comparing(MovimientoInventario::getMovimientoCantidad,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(MovimientoInventario::getMovimientoFecha,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        };

        if ("desc".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }
        movimientos.sort(comparator);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String[] headers = { "ID", "Fecha", "Producto", "Tipo", "Cantidad", "Stock Ant.", "Stock Nuevo", "Motivo" };
        List<String[]> datos = movimientos.stream()
                .map(m -> new String[] {
                        String.valueOf(m.getMovimientoId()),
                        m.getMovimientoFecha() != null ? m.getMovimientoFecha().format(dateFormatter) : "",
                        m.getProducto() != null ? m.getProducto().getProductoNombre() : "",
                        m.getMovimientoTipo() != null ? m.getMovimientoTipo() : "",
                        m.getMovimientoCantidad() != null ? String.valueOf(m.getMovimientoCantidad()) : "0",
                        m.getMovimientoStockAnterior() != null ? String.valueOf(m.getMovimientoStockAnterior()) : "-",
                        m.getMovimientoStockNuevo() != null ? String.valueOf(m.getMovimientoStockNuevo()) : "-",
                        m.getMovimientoMotivo() != null ? m.getMovimientoMotivo() : ""
                })
                .collect(Collectors.toList());

        String subtitulo = generarSubtituloMovimientos(tipo, productoId, fechaInicio, fechaFin);
        byte[] pdf = pdfReportService.generarReporte("Reporte de Movimientos de Inventario", subtitulo, headers, datos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_movimientos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private String generarSubtituloMovimientos(String tipo, Integer productoId,
            LocalDate fechaInicio, LocalDate fechaFin) {
        List<String> filtros = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (tipo != null && !tipo.isEmpty())
            filtros.add("Tipo: " + tipo);
        if (productoId != null) {
            Producto producto = productoService.findById(productoId).orElse(null);
            if (producto != null)
                filtros.add("Producto: " + producto.getProductoNombre());
        }
        if (fechaInicio != null)
            filtros.add("Desde: " + fechaInicio.format(dateFormatter));
        if (fechaFin != null)
            filtros.add("Hasta: " + fechaFin.format(dateFormatter));

        return filtros.isEmpty() ? "Todos los movimientos" : "Filtros: " + String.join(" | ", filtros);
    }
}
