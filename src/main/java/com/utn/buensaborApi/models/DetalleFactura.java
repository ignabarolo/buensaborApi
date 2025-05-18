package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DetalleFactura extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "id_factura")
    @JsonIgnore
    private Factura factura;
}
