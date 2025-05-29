package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ArticuloManufacturadoDetalle extends BaseEntity {
    private Double cantidad;

    @ManyToOne
    @JoinColumn(name = "articuloInsumo_id")
    private ArticuloInsumo articuloInsumo;

    @ManyToOne
    @JoinColumn(name = "articuloManufacturado_id")
    @JsonBackReference
    private ArticuloManufacturado articuloManufacturado;
}