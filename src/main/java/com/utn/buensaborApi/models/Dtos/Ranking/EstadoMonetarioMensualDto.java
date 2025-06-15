package com.utn.buensaborApi.models.Dtos.Ranking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadoMonetarioMensualDto {
    private Integer anio;
    private Integer mes;
    private Double ingreso;
    private Double costo;
    private Double ganancia;

    public EstadoMonetarioMensualDto(Integer anio, Integer mes, Double ingreso, Double costo) {
        this.anio = anio;
        this.mes = mes;
        this.ingreso = ingreso;
        this.costo = costo;
        this.ganancia = (ingreso != null && costo != null) ? ingreso - costo : null;
    }
}
