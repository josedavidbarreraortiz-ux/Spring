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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final AtributoService atributoService;
    private final ProductoAtributoRepository productoAtributoRepository;
    private final AtributoValorRepository atributoValorRepository;

    public ProductoController(ProductoService productoService,
            CategoriaService categoriaService,
            AtributoService atributoService,
            ProductoAtributoRepository productoAtributoRepository,
            AtributoValorRepository atributoValorRepository) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.atributoService = atributoService;
        this.productoAtributoRepository = productoAtributoRepository;
        this.atributoValorRepository = atributoValorRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "admin/productos/index";
    }

    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("atributos", atributoService.findAll());

        // Cargar valores de color (atributo_id = 1 según tu base de datos)
        model.addAttribute("valoresColor", atributoValorRepository.findByAtributoId(1));

        return "admin/productos/create";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto,
            @RequestParam(required = false, name = "atributoValor.id") Integer valorId,
            @RequestParam(required = false) Map<String, String> allParams) {

        Producto productoGuardado = productoService.save(producto);

        // Guardar/actualizar el color seleccionado
        if (valorId != null) {
            try {
                // Primero eliminar el color anterior si existe (atributo_id = 1 es Color)
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

                // Ahora guardar el nuevo color
                AtributoValor atributoValor = atributoValorRepository.findById(valorId).orElse(null);
                if (atributoValor != null) {
                    ProductoAtributo pa = new ProductoAtributo();
                    pa.setProducto(productoGuardado);
                    pa.setAtributo(atributoValor.getAtributo());
                    pa.setValor(atributoValor);
                    productoAtributoRepository.save(pa);
                }
            } catch (Exception e) {
                // Log error
                e.printStackTrace();
            }
        }

        // Procesar otros atributos dinámicos si existen
        if (allParams != null) {
            allParams.forEach((key, value) -> {
                if (key.startsWith("atributos[") && key.contains("][atributoId]") && value != null
                        && !value.isEmpty()) {
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
                        // Ignorar errores de parsing
                    }
                }
            });
        }
        return "redirect:/admin/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Producto producto = productoService.findById(id).orElse(null);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("atributos", atributoService.findAll());

        // Cargar valores de color (atributo_id = 1)
        model.addAttribute("valoresColor", atributoValorRepository.findByAtributoId(1));

        // Buscar el color actual del producto
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

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.delete(id);
        return "redirect:/admin/productos";
    }
}
