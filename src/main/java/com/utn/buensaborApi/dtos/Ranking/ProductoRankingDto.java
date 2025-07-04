package com.utn.buensaborApi.dtos.Ranking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoRankingDto {
    private String nombre;
    private Long cantidadVendida;
    private String categoria;
}
