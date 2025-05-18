package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Promocion extends BaseEntity {
    private String denominacion;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Double descuento;

//    @ManyToOne
//    @JoinColumn(name = "id_sucursal")
//    @JsonIgnore
//    private Sucursal sucursal;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromocionDetalle> promocionesDetalle;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoVentaDetalle> pedidosVentaDetalle;
//
//    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Imagen> imagenes;
}
