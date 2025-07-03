package com.utn.buensaborApi.dtos.Pedido;

import com.utn.buensaborApi.models.Articulo;
import com.utn.buensaborApi.models.base.BaseEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoVentaDetalleDto extends BaseEntity {
    private Integer cantidad;
    private Double subtotal;
    private Double subtotalCosto;

    private Articulo articulo;
    private PromocionDto promocion;


    // Constructor para artículo
    public PedidoVentaDetalleDto(Integer cantidad, Double subtotal, Double subtotalCosto, Articulo articulo) {
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.subtotalCosto = subtotalCosto;
        this.articulo = articulo;
        this.promocion = null;
    }

    // Constructor para promoción
    public PedidoVentaDetalleDto(Integer cantidad, Double subtotal, Double subtotalCosto, PromocionDto promocion) {
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.subtotalCosto = subtotalCosto;
        this.articulo = null;
        this.promocion = promocion;
    }
}
