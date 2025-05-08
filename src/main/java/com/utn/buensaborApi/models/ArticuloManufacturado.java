package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ArticuloManufacturado extends Articulo {
    private Integer tiempoEstimadoMinutos;
    private String descripcion;
    private Double precioCosto;

    @OneToMany(mappedBy = "articuloManufacturado", cascade = CascadeType.ALL)
    private List<ArticuloManufacturadoDetalle> detalles;
}