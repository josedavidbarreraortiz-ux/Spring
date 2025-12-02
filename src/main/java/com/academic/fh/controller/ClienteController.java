package com.academic.fh.controller;

import com.academic.fh.model.Cliente;
import com.academic.fh.service.ClienteService;
import com.academic.fh.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UserService userService;

    public ClienteController(ClienteService clienteService,
            UserService userService) {
        this.clienteService = clienteService;
        this.userService = userService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        return "admin/clientes/index";
    }

    // VER DETALLE - HABILITADO
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        Cliente cliente = clienteService.findById(id).orElse(null);
        model.addAttribute("cliente", cliente);
        return "admin/clientes/show";
    }

    // CREAR Y GUARDAR - DESHABILITADO
    // @GetMapping("/crear")
    // public String crearForm(Model model) {
    // model.addAttribute("cliente", new Cliente());
    // model.addAttribute("usuarios", userService.findAll());
    // return "admin/clientes/create";
    // }

    // @PostMapping("/guardar")
    // public String guardar(@ModelAttribute Cliente cliente) {
    // clienteService.save(cliente);
    // return "redirect:/admin/clientes";
    // }

    // EDITAR Y ELIMINAR - DESHABILITADO
    // @GetMapping("/editar/{id}")
    // public String editar(@PathVariable Integer id, Model model) {
    // Cliente cliente = clienteService.findById(id).orElse(null);
    // model.addAttribute("cliente", cliente);
    // model.addAttribute("usuarios", userService.findAll());
    // return "admin/clientes/edit";
    // }

    // @PostMapping("/eliminar/{id}")
    // public String eliminar(@PathVariable Integer id) {
    // clienteService.delete(id);
    // return "redirect:/admin/clientes";
    // }
}
