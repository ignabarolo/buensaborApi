package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.enums.Estado;
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
    @Query(
            value = "SELECT sub.denominacion as nombre, CAST(SUM(sub.cantidad_total) AS SIGNED) as cantidadVendida, sub.categoria " +
                    "FROM ( " +
                    "  SELECT a.id as articulo_id, a.denominacion, c.denominacion as categoria, SUM(d.cantidad) as cantidad_total " +
                    "  FROM pedido_venta pv " +
                    "  JOIN pedido_venta_detalle d ON pv.id = d.id_pedido_venta " +
                    "  JOIN articulo a ON d.id_articulo = a.id " +
                    "  JOIN categoria_articulo c ON a.categoria_id = c.id " +
                    "  WHERE pv.fecha_pedido BETWEEN :fechaInicio AND :fechaFin " +
                    "    AND pv.fecha_baja IS NULL " +
                    "  GROUP BY a.id, a.denominacion, c.denominacion " +
                    "  UNION ALL " +
                    "  SELECT a.id as articulo_id, a.denominacion, c.denominacion as categoria, SUM(d.cantidad * dp.cantidad) as cantidad_total " +
                    "  FROM pedido_venta pv " +
                    "  JOIN pedido_venta_detalle d ON pv.id = d.id_pedido_venta " +
                    "  JOIN promocion p ON d.id_promocion = p.id " +
                    "  JOIN promocion_detalle dp ON p.id = dp.id_promocion " +
                    "  JOIN articulo a ON dp.id_articulo = a.id " +
                    "  JOIN categoria_articulo c ON a.categoria_id = c.id " +
                    "  WHERE pv.fecha_pedido BETWEEN :fechaInicio AND :fechaFin " +
                    "    AND pv.fecha_baja IS NULL " +
                    "  GROUP BY a.id, a.denominacion, c.denominacion " +
                    ") as sub " +
                    "GROUP BY sub.articulo_id, sub.denominacion, sub.categoria " +
                    "ORDER BY cantidadVendida DESC",
            nativeQuery = true
    )
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

    @Query("SELECT p FROM PedidoVenta p JOIN FETCH p.cliente WHERE p.estado = :estado")
    List<PedidoVenta> findByEstadoConCliente(@Param("estado") Estado estado);

    List<PedidoVenta> findByEstado(Estado estado);
}
