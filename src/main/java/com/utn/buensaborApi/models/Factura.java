package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.buensaborApi.enums.FormaPago;
import com.utn.buensaborApi.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Factura extends BaseEntity {
    private LocalDate fechaFacturacion;
    private Integer nroComprobante;
    private FormaPago formaPago;
    private Double descuento;
    private Double gastoEnvio;
    private Double totalVenta;

    @ManyToOne
    @JoinColumn(name = "id_pedido_venta")
    @JsonIgnore
    private PedidoVenta pedidoVenta;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FacturaDetalle> facturaDetalles;

    @OneToOne(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DatoMercadoPago datoMercadoPago;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @JsonIgnore
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    @JsonIgnore
    private SucursalEmpresa sucursal;

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
