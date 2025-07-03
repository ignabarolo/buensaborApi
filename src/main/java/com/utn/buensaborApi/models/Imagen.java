package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.buensaborApi.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Imagen extends BaseEntity {
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "articulo_manufacturado_id")
    @JsonIgnore
    private ArticuloManufacturado articuloManufacturado;


    @ManyToOne
    @JoinColumn(name = "articulo_insumo_id")
    @JsonIgnore
    private ArticuloInsumo articuloInsumo;

    @ManyToOne
    @JoinColumn(name = "promocion_id")
    @JsonIgnore
    private Promocion promocion;

}
