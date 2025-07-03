package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.utn.buensaborApi.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoArticulo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArticuloManufacturado.class, name = "manufacturado"),
        @JsonSubTypes.Type(value = ArticuloInsumo.class, name = "insumo")
})
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
    private CategoriaArticulo categoria;

    //Metodo para Calcular precio de Venta
    protected abstract Double obtenerCostoBase();

    public void precioCalculado() {
        Double costo = obtenerCostoBase();
        if (costo == null || margenGanancia == null) {
            throw new IllegalStateException("No se puede calcular precio: falta costo o margen");
        }
        this.precioVenta = Math.round(costo * (1 + (margenGanancia / 100)) * 100.0) / 100.0;
    }

    //Metodo para obtener Estado en funcion del Stock disponible
       public abstract boolean obtenerEstado();

    //Metodo para obtener el tipo de articulo
    public String getTipoArticulo() {
        if (this instanceof ArticuloInsumo) {
            return "insumo";
        } else if (this instanceof ArticuloManufacturado) {
            return "manufacturado";
        }
        return null;
    }
}