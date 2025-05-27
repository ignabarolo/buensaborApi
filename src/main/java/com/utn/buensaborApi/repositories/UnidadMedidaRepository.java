package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Long> {
    List<UnidadMedida> findByFechaBajaIsNull();
    Optional<UnidadMedida> findByIdAndFechaBajaIsNull(Long id);
}