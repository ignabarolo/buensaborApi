package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ArticuloManufacturadoDetalle extends BaseEntity {
    private Double cantidad;

    @ManyToOne
    private SucursalInsumo articuloInsumo;

    @ManyToOne
    private ArticuloManufacturado articuloManufacturado;
}