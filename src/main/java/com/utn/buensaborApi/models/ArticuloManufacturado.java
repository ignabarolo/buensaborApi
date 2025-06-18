package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ArticuloManufacturado extends Articulo {
    private Integer tiempoEstimadoMinutos;
    private String descripcion;
    private Double precioCosto;

    @OneToMany(mappedBy = "articuloManufacturado", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ArticuloManufacturadoDetalle> detalles = new ArrayList<>();


    //Metodo heredado para determinar el costo sobre el que se calculará el precio de Venta
    @Override
    protected Double obtenerCostoBase() {
        return precioCosto;
    }

    //Metodo para calcular el costo total del ArticuloManifacturado
    public void costoCalculado() {
        if (detalles == null || detalles.isEmpty()) {
            System.out.println("No hay detalles en el ArticuloManufacturado");
            this.precioCosto = 0.0;
            return;
        }
        double total = 0.0;
        for (ArticuloManufacturadoDetalle detalle : detalles) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();
            if (insumo != null && insumo.getPrecioCompra() != null && detalle.getCantidad() != null) {
                double subtotal = insumo.getPrecioCompra() * detalle.getCantidad();
                total += subtotal;
            }
        }
        // Redondear a dos decimales
        this.precioCosto = Math.round(total * 100.0) / 100.0;
    }

    //Metodo para calcular stock disponible del articuloManufatcturado
    public int stockCalculado() {
        if (this.getSucursal() == null || this.detalles == null || detalles.isEmpty()) {
            return 0;
        }

        int stockMaxFabricable = Integer.MAX_VALUE;

        for (ArticuloManufacturadoDetalle detalle : detalles) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();

            if (insumo == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                return 0;
            }

            SucursalInsumo stockSucursalInsumo = insumo.getStockPorSucursal().stream()
                    .filter(stock -> stock.getSucursal().equals(this.getSucursal()))
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

    //Metodo para obtener el estado en funcion del stock disponible
    @Override
    public boolean obtenerEstado() {
        for (ArticuloManufacturadoDetalle detalle : detalles) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();
            if (insumo != null) {
                boolean stockSuficiente = insumo.getStockPorSucursal().stream()
                        .allMatch(sucursalInsumo -> sucursalInsumo.getStockActual() > sucursalInsumo.getStockMinimo());
                if (!stockSuficiente) {
                    return false;
                }
            }
        }
        return true;
    }

    // Metodo para obtener disponible
    public Integer stockCalculadoPorSucursal(Long idSucursal) {
        if (this.detalles == null || detalles.isEmpty()) {
            return 0;
        }

        int stockFabricable = Integer.MAX_VALUE;

        for (ArticuloManufacturadoDetalle detalle : detalles) {
            ArticuloInsumo insumo = detalle.getArticuloInsumo();

            if (insumo == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                return 0;
            }

            // Obtener stock del insumo en la sucursal específica
            int stockInsumoSucursal = insumo.obtenerStockEnSucursal(idSucursal);
            int unidadesPosibles = (int) (stockInsumoSucursal / detalle.getCantidad());

            if (unidadesPosibles < stockFabricable) {
                stockFabricable = unidadesPosibles;
            }
        }

        return stockFabricable;
    }
}