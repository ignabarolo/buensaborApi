package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.buensaborApi.enums.Estado;
import com.utn.buensaborApi.enums.FormaPago;
import com.utn.buensaborApi.enums.TipoEnvio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
public class PedidoVenta extends BaseEntity {
    private LocalDate fechaPedido;
    private LocalTime horaPedido;
    private Estado estado;
    private TipoEnvio tipoEnvio;
    private Double gastoEnvio;
    private FormaPago formaPago;
    private Double descuento;
    private Double totalCosto;
    private Double totalVenta;

    @OneToMany(mappedBy = "pedidoVenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoVentaDetalle> pedidosVentaDetalle;

    @OneToMany(mappedBy = "pedidoVenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas;

//    @ManyToOne
//    @JoinColumn(name = "id_sucursal")
//    @JsonIgnore
//    private Sucursal sucursal;
//
//    @ManyToOne
//    @JoinColumn(name = "id_domicilio")
//    @JsonIgnore
//    private Domicilio domicilio;
//
//    @ManyToOne
//    @JoinColumn(name = "id_cliente")
//    @JsonIgnore
//    private Cliente cliente;

    public LocalTime HoraFinalizacion() {
        return this.horaPedido;
    }

    public Double DescuentosCalculados() {
        return 0D;
    }

    public Double TotalCalculado() {
        return 0D;
    }

    public Double TotalCosto() {
        return 0D;
    }
}
