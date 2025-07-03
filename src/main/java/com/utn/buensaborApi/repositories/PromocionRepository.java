package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromocionRepository extends BaseRepository<Promocion, Long> {
    @Query("SELECT p FROM Promocion p WHERE p.fechaDesde <= :fecha AND p.fechaHasta >= :fecha AND p.fechaBaja IS NULL")
    List<Promocion> findActivePromotions(@Param("fecha") LocalDate fecha);
}