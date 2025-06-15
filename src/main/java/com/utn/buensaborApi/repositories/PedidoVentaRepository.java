package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Dtos.Ranking.ClienteRankingDto;
import com.utn.buensaborApi.models.Dtos.Ranking.ProductoRankingDto;
import com.utn.buensaborApi.models.Dtos.Ranking.EstadoMonetarioDto;
import com.utn.buensaborApi.models.Dtos.Ranking.EstadoMonetarioMensualDto;
import com.utn.buensaborApi.models.PedidoVenta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoVentaRepository  extends BaseRepository <PedidoVenta, Long>{

    List<PedidoVenta> findByClienteId(Long clienteId);

    // Listar pedidos por cliente y fecha
    @Query("SELECT p FROM PedidoVenta p WHERE p.cliente.id = :clienteId AND p.fechaPedido BETWEEN :fechaDesde AND :fechaHasta")
    List<PedidoVenta> findByClienteIdAndFechaPedidoBetween(@Param("clienteId") Long clienteId,
                                                           @Param("fechaDesde") LocalDate fechaDesde,
                                                           @Param("fechaHasta") LocalDate fechaHasta);
    // Ranking de productos
    @Query("SELECT new com.utn.buensaborApi.models.Dtos.Ranking.ProductoRankingDto(a.denominacion, SUM(d.cantidad), c.denominacion) " +
            "FROM PedidoVenta pv " +
            "JOIN pv.pedidosVentaDetalle d " +
            "JOIN d.articulo a " +
            "JOIN a.categoria c " +
            "WHERE pv.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
            "AND pv.fechaBaja IS NULL " +
            "GROUP BY a.denominacion, c.denominacion " +
            "ORDER BY SUM(d.cantidad) DESC")
    List<ProductoRankingDto> obtenerRankingProductosConCategoria(@Param("fechaInicio") LocalDate fechaInicio,
                                                                 @Param("fechaFin") LocalDate fechaFin);

    // Ranking por cliente
    @Query("SELECT new com.utn.buensaborApi.models.Dtos.Ranking.ClienteRankingDto(" +
            "c.id, CONCAT(c.nombre, ' ', c.apellido), COUNT(pv), SUM(pv.totalVenta)) " +
            "FROM PedidoVenta pv " +
            "JOIN pv.cliente c " +
            "WHERE pv.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
            "AND pv.fechaBaja IS NULL " +
            "GROUP BY c.id, c.nombre, c.apellido " +
            "ORDER BY " +
            "CASE WHEN :orden = 'cantidad' THEN COUNT(pv) END DESC, " +
            "CASE WHEN :orden = 'importe' THEN SUM(pv.totalVenta) END DESC")
    List<ClienteRankingDto> obtenerRankingClientes(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("orden") String orden);

    // Estadísticas totales de ventas
    @Query("SELECT new com.utn.buensaborApi.models.Dtos.Ranking.EstadoMonetarioDto(" +
            "SUM(pv.totalVenta), SUM(pv.totalCosto)) " +
            "FROM PedidoVenta pv " +
            "WHERE pv.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
            "AND pv.fechaBaja IS NULL")
    EstadoMonetarioDto obtenerTotalesEntreFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    // Estadísticas ventas mensuales
    @Query("SELECT new com.utn.buensaborApi.models.Dtos.Ranking.EstadoMonetarioMensualDto(" +
            "YEAR(pv.fechaPedido), MONTH(pv.fechaPedido), SUM(pv.totalVenta), SUM(pv.totalCosto)) " +
            "FROM PedidoVenta pv " +
            "WHERE pv.fechaPedido BETWEEN :fechaInicio AND :fechaFin " +
            "AND pv.fechaBaja IS NULL " +
            "GROUP BY YEAR(pv.fechaPedido), MONTH(pv.fechaPedido) " +
            "ORDER BY YEAR(pv.fechaPedido), MONTH(pv.fechaPedido)")
    List<EstadoMonetarioMensualDto> obtenerTotalesMensualesEntreFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}
