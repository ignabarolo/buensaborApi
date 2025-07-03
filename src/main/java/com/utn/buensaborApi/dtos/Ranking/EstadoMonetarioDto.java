package com.utn.buensaborApi.dtos.Ranking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadoMonetarioDto {
    private Double ingreso;
    private Double costo;
    private Double ganancia;

    public EstadoMonetarioDto(Double ingreso, Double costo) {
        this.ingreso = ingreso;
        this.costo = costo;
        this.ganancia = (ingreso != null && costo != null) ? ingreso - costo : null;
    }
}
