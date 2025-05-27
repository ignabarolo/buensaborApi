package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticuloManufacturadoRepository extends JpaRepository<ArticuloManufacturado, Long> {
    @Query("SELECT a FROM ArticuloManufacturado a " +
            "WHERE a.id = :id AND a.fechaBaja IS NULL")
    Optional<ArticuloManufacturado> findByIdNoDetails(@Param("id") Long id);

}
