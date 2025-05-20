package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class FacturaDetalle extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "id_factura")
    @JsonIgnore
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;
}
