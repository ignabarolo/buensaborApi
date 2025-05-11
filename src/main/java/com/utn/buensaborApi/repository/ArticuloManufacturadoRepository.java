package com.utn.buensaborApi.repository;

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

    @Query("SELECT DISTINCT a FROM ArticuloManufacturado a " +
            "LEFT JOIN FETCH a.detalles d " +
            "LEFT JOIN FETCH d.articuloInsumo " +
            "WHERE a.id = :id AND a.fechaBaja IS NULL")
    Optional<ArticuloManufacturado> findByIdWithDetails(@Param("id") Long id);

}
