package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Articulo extends BaseEntity {
    private String denominacion;
    private Double precioVenta;
    private Double margenGanancia;

    @ManyToOne
    @JoinColumn(name = "unidadMedida_id")
    private UnidadMedida unidadMedida;

    @ManyToOne
    @JoinColumn(name = "sucursal_id", referencedColumnName = "id")
    @JsonIgnore
    private SucursalEmpresa sucursal;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "articulo_id")
    private List<Imagen> imagenes;

    @ManyToOne
    @JoinColumn(name ="categoria_id")
    @JsonIgnore
    private CategoriaArticulo categoria;

    //Metodo para Calcular precio de Venta
    protected abstract Double obtenerCostoBase();

    public void precioCalculado() {
        Double costo = obtenerCostoBase();
        if (costo == null || margenGanancia == null) {
            throw new IllegalStateException("No se puede calcular precio: falta costo o margen");
        }
        this.precioVenta = costo * (1 + (margenGanancia/100));
    }
}