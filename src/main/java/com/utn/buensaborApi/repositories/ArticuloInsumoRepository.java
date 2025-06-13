package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.ArticuloInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticuloInsumoRepository extends JpaRepository<ArticuloInsumo, Long> {

    @Query("SELECT DISTINCT a FROM ArticuloInsumo a " +
            "LEFT JOIN FETCH a.stockPorSucursal s " +
            "WHERE a.id = :id AND a.fechaBaja IS NULL")
    Optional<ArticuloInsumo> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT a FROM ArticuloInsumo a " +
            "LEFT JOIN FETCH a.stockPorSucursal s " +
            "WHERE s.sucursal.id = :sucursalId " +
            "AND a.fechaBaja IS NULL")
    List<ArticuloInsumo> findAllBySucursalWithDetails(@Param("sucursalId") Long sucursalId);

    @Query("SELECT DISTINCT a FROM ArticuloInsumo a " +
            "LEFT JOIN FETCH a.stockPorSucursal s " +
            "WHERE s.sucursal.id = :sucursalId " +
            "AND a.esParaElaborar = true " +
            "AND a.fechaBaja IS NULL")
    List<ArticuloInsumo> findAllForElaborationBySucursal(@Param("sucursalId") Long sucursalId);

    @Query("SELECT DISTINCT a FROM ArticuloInsumo a " +
            "LEFT JOIN FETCH a.stockPorSucursal s " +
            "LEFT JOIN FETCH a.categoria c " +
            "WHERE a.fechaBaja IS NULL")
    List<ArticuloInsumo> findAllWithDetails();

}
