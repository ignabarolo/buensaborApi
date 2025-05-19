package com.utn.buensaborApi.repository;

import com.utn.buensaborApi.models.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByFechaBajaIsNull();
    Optional<Imagen> findByIdAndFechaBajaIsNull(Long id);
}