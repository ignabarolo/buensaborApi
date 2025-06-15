package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Dtos.Ranking.ClienteRankingDto;
import com.utn.buensaborApi.models.Dtos.Ranking.ProductoRankingDto;
import com.utn.buensaborApi.models.PedidoVenta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoVentaRepository  extends BaseRepository <PedidoVenta, Long>{

    List<PedidoVenta> findByClienteId(Long clienteId);

    //Consulta ranking por Producto
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

    //consulta ranking por cliente
    @Query("SELECT new com.utn.buensaborApi.models.Dtos.Ranking.ClienteRankingDto(" +
            "c.id, CONCAT(c.nombre, ' ', c.apellido), COUNT(pv), SUM(pv.totalVenta)) " +
            "FROM PedidoVenta pv " +
            "JOIN pv.cliente c " +
            "WHERE pv.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY c.id, c.nombre, c.apellido " +
            "ORDER BY " +
            "CASE WHEN :orden = 'cantidad' THEN COUNT(pv) END DESC, " +
            "CASE WHEN :orden = 'importe' THEN SUM(pv.totalVenta) END DESC")
    List<ClienteRankingDto> obtenerRankingClientes(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("orden") String orden);
}
