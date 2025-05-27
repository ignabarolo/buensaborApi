package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.ArticuloManufacturadoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloManufacturadoDetalleRepository extends JpaRepository<ArticuloManufacturadoDetalle, Long> {
    @Query("SELECT d FROM ArticuloManufacturadoDetalle d " +
            "LEFT JOIN FETCH d.articuloInsumo " +
            "WHERE d.articuloManufacturado.id = :articuloId AND d.fechaBaja IS NULL")
    List<ArticuloManufacturadoDetalle> findByArticuloManufacturadoId(@Param("articuloId") Long articuloId);

}
