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

        model.addAttribute("carrito", carritoService.getCarrito(session));

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
}
