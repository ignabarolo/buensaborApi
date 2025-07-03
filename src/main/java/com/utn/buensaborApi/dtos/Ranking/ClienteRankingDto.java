package com.utn.buensaborApi.dtos.Ranking;

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
