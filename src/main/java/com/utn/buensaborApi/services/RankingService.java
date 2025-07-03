package com.utn.buensaborApi.services;

import com.utn.buensaborApi.dtos.Ranking.ClienteRankingDto;
import com.utn.buensaborApi.dtos.Ranking.EstadoMonetarioDto;
import com.utn.buensaborApi.dtos.Ranking.EstadoMonetarioMensualDto;
import com.utn.buensaborApi.dtos.Ranking.ProductoRankingDto;
import com.utn.buensaborApi.repositories.PedidoVentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RankingService {
    private final PedidoVentaRepository pedidoVentaRepository;

    public RankingService(PedidoVentaRepository pedidoVentaRepository) {
        this.pedidoVentaRepository = pedidoVentaRepository;
    }

    //Ranking de productos vendidos
    public Map<String, List<ProductoRankingDto>> obtenerRankingProductos(LocalDate desde, LocalDate hasta) {
        List<ProductoRankingDto> resultados = pedidoVentaRepository.obtenerRankingProductosConCategoria(desde, hasta);

        Map<String, List<ProductoRankingDto>> ranking = new HashMap<>();
        ranking.put("bebida", new ArrayList<>());
        ranking.put("comida", new ArrayList<>());

        for (ProductoRankingDto dto : resultados) {
            if ("bebida".equalsIgnoreCase(dto.getCategoria())) {
                ranking.get("bebida").add(dto);
            } else {
                ranking.get("comida").add(dto);
            }
        }
        return ranking;
    }

    //Ranking de clientes
    public List<ClienteRankingDto> obtenerRankingClientes(LocalDate desde, LocalDate hasta, String orden) {
        return pedidoVentaRepository.obtenerRankingClientes(desde, hasta, orden);
    }

    // Estadísticas totales de ventas
    public EstadoMonetarioDto obtenerTotales(LocalDate desde, LocalDate hasta) {
        return pedidoVentaRepository.obtenerTotalesEntreFechas(desde, hasta);
    }

    // Estadísticas ventas mensuales
    public List<EstadoMonetarioMensualDto> obtenerTotalesMensuales(LocalDate desde, LocalDate hasta) {
        return pedidoVentaRepository.obtenerTotalesMensualesEntreFechas(desde, hasta);
    }
}
