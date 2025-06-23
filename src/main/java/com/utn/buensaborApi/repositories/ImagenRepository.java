package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Imagen;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByFechaBajaIsNull();
    Optional<Imagen> findByIdAndFechaBajaIsNull(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Imagen i WHERE i.articuloManufacturado.id = :articuloId")
    void deleteByArticuloManufacturadoId(@Param("articuloId") Long articuloId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Imagen i WHERE i.promocion.id = :promocionId")
    void deleteByPromocionId(@Param("promocionId") Long promocionId);

     @Query("SELECT i FROM Imagen i WHERE i.id = :id AND i.fechaBaja IS NOT NULL")
     Optional<Imagen> findByIdAndFechaBajaIsNotNull(@Param("id") Long id);
}