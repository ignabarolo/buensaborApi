package com.utn.buensaborApi.dtos.Pedido;

import com.utn.buensaborApi.models.Articulo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionDetalleDto {
    private Integer cantidad;
    private Double subtotal;
    private Double subtotalCosto;

    private Articulo articulo;
}
