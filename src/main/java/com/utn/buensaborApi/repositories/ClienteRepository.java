package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByUsuario_Auth0id(String auth0id); 
}
