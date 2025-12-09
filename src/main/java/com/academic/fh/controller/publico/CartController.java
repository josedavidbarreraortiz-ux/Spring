package com.academic.fh.controller.publico;

import com.academic.fh.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
public class CartController {

    private final CarritoService carritoService;

    public CartController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public String verCarrito(Model model, HttpSession session) {

        var carrito = carritoService.getCarrito(session);
        model.addAttribute("carrito", carrito);

        // Calcular total para evitar lambdas en Thymeleaf (SpEL no las soporta)
        double total = 0;
        for (var item : carrito) {
            Double precio = Double.valueOf(item.get("precio").toString());
            Integer cantidad = Integer.valueOf(item.get("cantidad").toString());
            total += precio * cantidad;
        }
        model.addAttribute("totalCarrito", total);

        return "cart";
    }

    @PostMapping("/agregar")
    public String agregar(
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            HttpSession session) {

        carritoService.agregarProducto(productoId, cantidad, session);

        return "redirect:/carrito";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam int index, HttpSession session) {

        carritoService.eliminarItem(index, session);

        return "redirect:/carrito";
    }

    @PostMapping("/vaciar")
    public String vaciar(HttpSession session) {
        carritoService.vaciarCarrito(session);
        return "redirect:/carrito";
    }

    @PostMapping("/actualizar")
    public String actualizarCantidad(
            @RequestParam int index,
            @RequestParam int cantidad,
            HttpSession session) {
        carritoService.actualizarCantidad(index, cantidad, session);
        return "redirect:/carrito";
    }
}
