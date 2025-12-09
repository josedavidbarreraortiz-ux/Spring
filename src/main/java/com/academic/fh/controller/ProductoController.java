package com.academic.fh.controller;

import com.academic.fh.model.Producto;
import com.academic.fh.model.ProductoAtributo;
import com.academic.fh.model.Atributo;
import com.academic.fh.service.ProductoService;
import com.academic.fh.service.CategoriaService;
import com.academic.fh.service.AtributoService;
import com.academic.fh.repository.ProductoAtributoRepository;
import com.academic.fh.repository.AtributoValorRepository;
import com.academic.fh.model.AtributoValor;
import com.academic.fh.model.Inventario;
import com.academic.fh.service.InventarioService;
import com.academic.fh.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final AtributoService atributoService;
    private final ProductoAtributoRepository productoAtributoRepository;
    private final AtributoValorRepository atributoValorRepository;
    private final InventarioService inventarioService;
    private final FileStorageService fileStorageService;

    public ProductoController(ProductoService productoService,
            CategoriaService categoriaService,
            AtributoService atributoService,
            ProductoAtributoRepository productoAtributoRepository,
            AtributoValorRepository atributoValorRepository,
            InventarioService inventarioService,
            FileStorageService fileStorageService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.atributoService = atributoService;
        this.productoAtributoRepository = productoAtributoRepository;
        this.atributoValorRepository = atributoValorRepository;
        this.inventarioService = inventarioService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Boolean mostrarTodos, Model model) {
        java.util.List<Producto> productos;
        if (mostrarTodos != null && mostrarTodos) {
            productos = productoService.findAll();
        } else {
            productos = productoService.findAllActivos();
        }
        model.addAttribute("productos", productos);
        model.addAttribute("mostrarTodos", mostrarTodos);
        return "admin/productos/index";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("atributos", atributoService.findAll());
        model.addAttribute("valoresColor", atributoValorRepository.findByAtributoId(1));
        model.addAttribute("marcasExistentes", productoService.findDistinctMarcas());
        return "admin/productos/create";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto,
            @RequestParam(required = false, name = "atributoValor.id") Integer valorId,
            @RequestParam(required = false) Map<String, String> allParams,
            @RequestParam(required = false, name = "imagenFile") MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        boolean esNuevo = producto.getProductoId() == null;
        String nombreProducto = producto.getProductoNombre();

        // Procesar imagen si se subió un archivo
        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                if (producto.getProductoId() != null) {
                    Producto productoExistente = productoService.findById(producto.getProductoId()).orElse(null);
                    if (productoExistente != null && productoExistente.getFoto() != null
                            && !fileStorageService.isExternalUrl(productoExistente.getFoto())) {
                        fileStorageService.deleteFile(productoExistente.getFoto(), "productos");
                    }
                }
                String filename = fileStorageService.storeFile(imagenFile, "productos");
                producto.setFoto(filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Producto productoGuardado = productoService.save(producto);

        // Guardar color seleccionado
        if (valorId != null) {
            try {
                if (productoGuardado.getAtributos() != null) {
                    ProductoAtributo colorAnterior = null;
                    for (ProductoAtributo pa : productoGuardado.getAtributos()) {
                        if (pa.getAtributo() != null && pa.getAtributo().getAtributoId() == 1) {
                            colorAnterior = pa;
                            break;
                        }
                    }
                    if (colorAnterior != null) {
                        productoAtributoRepository.delete(colorAnterior);
                    }
                }

                AtributoValor atributoValor = atributoValorRepository.findById(valorId).orElse(null);
                if (atributoValor != null) {
                    ProductoAtributo pa = new ProductoAtributo();
                    pa.setProducto(productoGuardado);
                    pa.setAtributo(atributoValor.getAtributo());
                    pa.setValor(atributoValor);
                    productoAtributoRepository.save(pa);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Procesar otros atributos dinámicos
        if (allParams != null) {
            allParams.forEach((key, value) -> {
                if (key.startsWith("atributos[") && key.contains("][atributoId]") && value != null && !value.isEmpty()) {
                    try {
                        String indexStr = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
                        String valorTextoKey = "atributos[" + indexStr + "][valorTexto]";
                        String valorTexto = allParams.get(valorTextoKey);

                        if (valorTexto != null && !valorTexto.isEmpty()) {
                            ProductoAtributo pa = new ProductoAtributo();
                            pa.setProducto(productoGuardado);
                            Integer atributoId = Integer.parseInt(value);
                            Atributo atributo = atributoService.findById(atributoId).orElse(null);
                            pa.setAtributo(atributo);
                            pa.setValorTexto(valorTexto);
                            productoAtributoRepository.save(pa);
                        }
                    } catch (Exception e) {
                        // Ignorar errores
                    }
                }
            });
        }

        // Crear inventario automáticamente si no existe
        if (inventarioService.findByProductoId(productoGuardado.getProductoId()).isEmpty()) {
            Inventario inventario = new Inventario();
            inventario.setProducto(productoGuardado);
            inventario.setInventarioStockActual(0);
            inventario.setInventarioStockMinimo(5);
            inventario.setInventarioStockMaximo(100);
            inventarioService.save(inventario);
        }

        // Mensaje flash
        if (esNuevo) {
            redirectAttributes.addFlashAttribute("successMessage", "Producto '" + nombreProducto + "' creado exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Producto '" + nombreProducto + "' actualizado exitosamente");
        }

        return "redirect:/admin/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Producto producto = productoService.findById(id).orElse(null);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("atributos", atributoService.findAll());
        model.addAttribute("valoresColor", atributoValorRepository.findByAtributoId(1));
        model.addAttribute("marcasExistentes", productoService.findDistinctMarcas());

        Integer colorActualId = null;
        if (producto != null && producto.getAtributos() != null) {
            for (ProductoAtributo pa : producto.getAtributos()) {
                if (pa.getAtributo() != null && pa.getAtributo().getAtributoId() == 1) {
                    if (pa.getValor() != null) {
                        colorActualId = pa.getValor().getValorId();
                    }
                    break;
                }
            }
        }
        model.addAttribute("colorActualId", colorActualId);

        return "admin/productos/edit";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        Producto producto = productoService.findById(id).orElse(null);
        model.addAttribute("producto", producto);
        return "admin/productos/show";
    }

    @PostMapping("/inactivar/{id}")
    public String inactivar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Producto producto = productoService.findById(id).orElse(null);
        if (producto != null) {
            producto.setProductoEstado("Inactivo");
            productoService.save(producto);
            redirectAttributes.addFlashAttribute("warningMessage", "Producto '" + producto.getProductoNombre() + "' inactivado");
        }
        return "redirect:/admin/productos?mostrarTodos=true";
    }

    @PostMapping("/activar/{id}")
    public String activar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Producto producto = productoService.findById(id).orElse(null);
        if (producto != null) {
            producto.setProductoEstado("Activo");
            productoService.save(producto);
            redirectAttributes.addFlashAttribute("successMessage", "Producto '" + producto.getProductoNombre() + "' activado");
        }
        return "redirect:/admin/productos";
    }

    @PostMapping("/color/nuevo")
    @ResponseBody
    public java.util.Map<String, Object> crearNuevoColor(@RequestParam String nombreColor) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            Atributo atributoColor = atributoService.findById(1).orElse(null);
            if (atributoColor == null) {
                response.put("success", false);
                response.put("mensaje", "No se encontró el atributo Color");
                return response;
            }

            AtributoValor nuevoColor = new AtributoValor();
            nuevoColor.setAtributo(atributoColor);
            nuevoColor.setValor(nombreColor);
            AtributoValor colorGuardado = atributoValorRepository.save(nuevoColor);

            response.put("success", true);
            response.put("valorId", colorGuardado.getValorId());
            response.put("valor", colorGuardado.getValor());
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al guardar: " + e.getMessage());
        }
        return response;
    }
}
