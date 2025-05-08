package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Articulo extends BaseEntity {
    protected String denominacion;
    protected Double precioVenta;
    protected Double margenGanancia;

    @ManyToOne
    protected UnidadMedida unidadMedida;

    @ManyToOne
    protected CategoriaArticulo categoria;
}