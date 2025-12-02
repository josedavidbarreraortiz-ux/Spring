package com.academic.fh.service;

import com.academic.fh.model.Cliente;
import com.academic.fh.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Integer id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void delete(Integer id) {
        clienteRepository.deleteById(id);
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findAll().stream()
                .filter(c -> c.getClienteEmail().equals(email))
                .findFirst();
    }

    public Optional<Cliente> findByUserId(Long userId) {
        return clienteRepository.findAll().stream()
                .filter(c -> c.getUser() != null && c.getUser().getId().equals(userId))
                .findFirst();
    }

    public Optional<Cliente> findByDocumento(String documento) {
        return clienteRepository.findAll().stream()
                .filter(c -> c.getClienteNumeroDocumento() != null && c.getClienteNumeroDocumento().equals(documento))
                .findFirst();
    }
}
