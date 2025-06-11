package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface clienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
