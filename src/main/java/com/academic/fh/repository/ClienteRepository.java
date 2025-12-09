package com.academic.fh.repository;

import com.academic.fh.model.Cliente;
import com.academic.fh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByUser(User user);

    Optional<Cliente> findByClienteNumeroDocumento(String documento);
}
