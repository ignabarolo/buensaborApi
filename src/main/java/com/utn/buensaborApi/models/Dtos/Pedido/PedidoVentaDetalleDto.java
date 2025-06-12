package com.utn.buensaborApi.models.Dtos.Pedido;

import com.utn.buensaborApi.models.Articulo;
import com.utn.buensaborApi.models.BaseEntity;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloManufacturadoDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoVentaDetalleDto extends BaseEntity {
    private Integer cantidad;
    private Double subtotal;
    private Double subtotalCosto;

    private Articulo articulo;
}
