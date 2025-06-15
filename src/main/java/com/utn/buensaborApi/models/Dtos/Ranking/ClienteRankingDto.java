package com.utn.buensaborApi.models.Dtos.Ranking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteRankingDto {
    private Long clienteId;
    private String clienteNome;
    private Long cantidadPedidos;
    private Double importeTotal;
}
