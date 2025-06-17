package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Promocion extends BaseEntity {
    private String denominacion;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Double descuento;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    @JsonIgnore
    private SucursalEmpresa sucursal;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromocionDetalle> promocionesDetalle;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoVentaDetalle> pedidosVentaDetalle;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes;


    //Calcular precio de venta con descuento
    public BigDecimal getPrecioVenta() {
        if (promocionesDetalle == null || promocionesDetalle.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Sumar precios de todos los artículos multiplicados por cantidad
        BigDecimal precioTotalSinDescuento = promocionesDetalle.stream()
                .filter(detalle -> detalle.getArticulo() != null)
                .map(detalle -> {
                    double precioArticulo = 0.0;
                    if (detalle.getArticulo() instanceof ArticuloManufacturado) {
                        precioArticulo = ((ArticuloManufacturado) detalle.getArticulo()).getPrecioVenta();
                    } else if (detalle.getArticulo() instanceof ArticuloInsumo) {
                        precioArticulo = ((ArticuloInsumo) detalle.getArticulo()).getPrecioVenta();
                    }
                    return BigDecimal.valueOf(precioArticulo)
                            .multiply(BigDecimal.valueOf(detalle.getCantidad()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Aplicar descuento
        if (descuento != null && descuento > 0) {
            BigDecimal factorDescuento = BigDecimal.ONE.subtract(
                    BigDecimal.valueOf(descuento).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
            );
            return precioTotalSinDescuento.multiply(factorDescuento).setScale(2, RoundingMode.HALF_UP);
        } else {
            return precioTotalSinDescuento.setScale(2, RoundingMode.HALF_UP);
        }
    }

    // Metodo para obtener el stock disponible de la promoción
    public Integer obtenerStockDisponible() {
        if (promocionesDetalle == null || promocionesDetalle.isEmpty()) {
            return 0;
        }

        return promocionesDetalle.stream()
                .map(detalle -> {
                    if (detalle.getArticulo() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                        return 0;
                    }

                    Integer stockDisponible;

                    if (detalle.getArticulo() instanceof ArticuloInsumo) {
                        stockDisponible = ((ArticuloInsumo) detalle.getArticulo()).obtenerStockMaximo();
                    } else if (detalle.getArticulo() instanceof ArticuloManufacturado) {
                        stockDisponible = ((ArticuloManufacturado) detalle.getArticulo()).stockMaximoCalculado();
                    } else {
                        return 0;
                    }

                    return stockDisponible / detalle.getCantidad();
                })
                .min(Integer::compare)
                .orElse(0);
    }
}
