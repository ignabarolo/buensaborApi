package com.utn.buensaborApi.models;

import jakarta.persistence.*;
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


    //Metodo heredado para determinar el costo sobre el que se calculará el precio de Venta
    @Override
    protected Double obtenerCostoBase() {
        return precioCompra;
    }

    //Metodo para determinar el estado segun el stock disponible
    @Override
    public boolean obtenerEstado() {
        return this.getStockPorSucursal().stream()
                .allMatch(sucursalInsumo -> sucursalInsumo.getStockActual() > sucursalInsumo.getStockMinimo());
    }

}
