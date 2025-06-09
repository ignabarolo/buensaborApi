package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter

public class PedidoVentaDetalle extends BaseEntity {
    private Integer cantidad;
    private Double subtotal;
    private Double subtotalCosto;

    @ManyToOne
    @JoinColumn(name = "id_promocion")
    private Promocion promocion;

    @ManyToOne
    @JoinColumn(name = "id_pedido_venta")
    @JsonIgnore
    private PedidoVenta pedidoVenta;

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

    public Double SubtotalCalculado() {
        return 0D;
    }

    public Double SubtotalCosto() {
        return 0D;
    }
}
