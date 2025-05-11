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

    @OneToMany(mappedBy = "articuloManufacturado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticuloManufacturadoDetalle> detalles;

    //Metodo heredado para determinar el costo sobre el que se calculará el precio de Venta
    @Override
    protected Double obtenerCostoBase() {
        return precioCosto;
    }

    //Metodo para calcular el costo total del ArticuloManifacturado
    public void costoCalculado() {
        if (detalles == null || detalles.isEmpty()) {
            this.precioCosto = 0.0;
            return;
        }

        double total = 0.0;
        for (ArticuloManufacturadoDetalle detalle : detalles) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();
            if (insumo != null && insumo.getPrecioCompra() != null && detalle.getCantidad() != null) {
                total += insumo.getPrecioCompra() * detalle.getCantidad();
            }
        }
        this.precioCosto = total;
    }

    //Metodo para calcular stock disponible del articuloManufatcturado
    public int stockCalculado() {
        if (this.sucursal == null || this.detalles == null || detalles.isEmpty()) {
            return 0;
        }

        int stockMaxFabricable = Integer.MAX_VALUE;

        for (ArticuloManufacturadoDetalle detalle : detalles) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();

            if (insumo == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                return 0;
            }

            SucursalInsumo stockSucursalInsumo = insumo.getStockPorSucursal().stream()
                    .filter(stock -> stock.getSucursal().equals(this.sucursal))
                    .findFirst()
                    .orElse(null);

            if (stockSucursalInsumo == null || stockSucursalInsumo.getStockActual() == null) {
                return 0;
            }

            int unidadesPosibles = (int) (stockSucursalInsumo.getStockActual() / detalle.getCantidad());

            if (unidadesPosibles < stockMaxFabricable) {
                stockMaxFabricable = unidadesPosibles;
            }
        }

        return stockMaxFabricable;
    }
}