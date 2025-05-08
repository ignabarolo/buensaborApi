package com.utn.buensaborApi.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ArticuloInsumo extends Articulo{
    private Double precioCompra;
    private Boolean esParaElaborar;

    @OneToMany(mappedBy = "articuloInsumo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SucursalInsumo> stockPorSucursal;


    //Método heredado para determinar el costo sobre el que se calculará el precio de Venta
    @Override
    protected Double obtenerCostoBase() {
        return precioCompra;
    }
}
