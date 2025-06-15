package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Dtos.Ranking.ProductoRankingDto;
import com.utn.buensaborApi.models.PedidoVenta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoVentaRepository  extends BaseRepository <PedidoVenta, Long>{

    List<PedidoVenta> findByClienteId(Long clienteId);

    //Consulta ranking de Productos
    @Query("SELECT new com.utn.buensaborApi.models.Dtos.Ranking.ProductoRankingDto(a.denominacion, SUM(d.cantidad), c.denominacion) " +
            "FROM PedidoVenta pv " +
            "JOIN pv.pedidosVentaDetalle d " +
            "JOIN d.articulo a " +
            "JOIN a.categoria c " +
            "WHERE pv.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY a.denominacion, c.denominacion " +
            "ORDER BY SUM(d.cantidad) DESC")
    List<ProductoRankingDto> obtenerRankingProductosConCategoria(@Param("fechaInicio") LocalDate fechaInicio,
                                                                 @Param("fechaFin") LocalDate fechaFin);
}
