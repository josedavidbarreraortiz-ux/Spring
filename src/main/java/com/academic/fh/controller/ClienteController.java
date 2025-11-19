package com.academic.fh.controller;

import com.academic.fh.model.Cliente;
import com.academic.fh.repository.ClienteRepository;
import com.academic.fh.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;

    public ClienteController(ClienteRepository clienteRepository,
                             UserRepository userRepository) {
        this.clienteRepository = clienteRepository;
        this.userRepository = userRepository;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        return "clientes/lista"; // templates/clientes/lista.html
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("usuarios", userRepository.findAll());
        return "clientes/form"; // templates/clientes/form.html
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        clienteRepository.save(cliente);
        return "redirect:/clientes";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        model.addAttribute("cliente", cliente);
        model.addAttribute("usuarios", userRepository.findAll());
        return "clientes/form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        clienteRepository.deleteById(id);
        return "redirect:/clientes";
    }
}
