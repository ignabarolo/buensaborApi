package com.utn.buensaborApi.dtos.Pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionDto {
    private int id;
    private String denominacion;
    private String descripcion;
    private Double descuento;
    private List<PromocionDetalleDto> promocionesDetalle;

}
