package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.utn.buensaborApi.enums.Estado;
import com.utn.buensaborApi.enums.FormaPago;
import com.utn.buensaborApi.enums.TipoEnvio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class PedidoVenta extends BaseEntity {
    private LocalDate fechaPedido;
    private LocalTime horaPedido;
    private Estado estado;
    private TipoEnvio tipoEnvio;
    private Double gastoEnvio;
    private FormaPago formaPago;
    private Double descuento;
    private Double totalCosto;
    private Double totalVenta;

    @OneToMany(mappedBy = "pedidoVenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoVentaDetalle> pedidosVentaDetalle;

    @OneToMany(mappedBy = "pedidoVenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    @JsonIgnore
    private SucursalEmpresa sucursal;

    @ManyToOne
    @JoinColumn(name = "id_domicilio")
    private Domicilio domicilio;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    @JsonIgnore
    private Empleado empleado;

    public LocalTime HoraFinalizacion() {
        return this.horaPedido;
    }

    public Double DescuentosCalculados() {
        return 0D;
    }

    public Double TotalCalculado() {
        return 0D;
    }

    public Double TotalCosto() {
        return 0D;
    }

    //Disminuir stock de insumos según los artículos del pedido
    public boolean disminuirStockInsumos() {
        if (this.sucursal == null || this.pedidosVentaDetalle == null || this.pedidosVentaDetalle.isEmpty()) {
            throw new RuntimeException("No se puede actualizar stock: datos incompletos en el pedido");
        }

        // Recorre cada detalle del pedido
        for (PedidoVentaDetalle detalle : this.pedidosVentaDetalle) {
            Integer cantidad = detalle.getCantidad();
            if (cantidad == null || cantidad <= 0) {
                continue;
            }

            // Si el detalle tiene un artículo directo
            if (detalle.getArticulo() != null) {
                actualizarStockPorArticulo(detalle.getArticulo(), cantidad);
            }

            // Si el detalle tiene una promoción
            if (detalle.getPromocion() != null && detalle.getPromocion().getPromocionesDetalle() != null) {
                for (PromocionDetalle promoDetalle : detalle.getPromocion().getPromocionesDetalle()) {
                    if (promoDetalle.getArticulo() != null && promoDetalle.getCantidad() != null) {
                        // Por cada artículo en la promoción, multiplicamos por la cantidad del detalle
                        int cantidadTotal = promoDetalle.getCantidad() * cantidad;
                        actualizarStockPorArticulo(promoDetalle.getArticulo(), cantidadTotal);
                    }
                }
            }
        }

        return true;
    }

    //Metodo auxiliar para actualizar el stock según el tipo de artículo

    private void actualizarStockPorArticulo(Articulo articulo, int cantidad) {
        if (articulo instanceof ArticuloInsumo) {
            // Si es insumo, actualizar directamente
            actualizarStockInsumo((ArticuloInsumo) articulo, cantidad);
        } else if (articulo instanceof ArticuloManufacturado) {
            // Si es manufacturado, recorrer sus detalles de insumos
            ArticuloManufacturado manufacturado = (ArticuloManufacturado) articulo;
            if (manufacturado.getDetalles() != null) {
                for (ArticuloManufacturadoDetalle manuDetalle : manufacturado.getDetalles()) {
                    if (manuDetalle.getArticuloInsumo() != null && manuDetalle.getCantidad() != null) {
                        // Calculamos cuánto insumo se necesita para la cantidad vendida
                        double cantidadInsumo = manuDetalle.getCantidad() * cantidad;
                        actualizarStockInsumo(manuDetalle.getArticuloInsumo(), cantidadInsumo);
                    }
                }
            }
        }
    }

    //Actualizar stock de un insumo específico en la sucursal
    private void actualizarStockInsumo(ArticuloInsumo insumo, double cantidad) {

        //Verificar si el insumo tiene stock en la sucursal
        if (insumo.getStockPorSucursal() == null) {
            throw new RuntimeException("El insumo " + insumo.getDenominacion() +
                    " no tiene información de stock en ninguna sucursal");
        }
        SucursalInsumo sucursalInsumo = insumo.getStockPorSucursal().stream()
                .filter(si -> si.getSucursal().getId().equals(this.sucursal.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró stock del insumo " + insumo.getDenominacion() + " en la sucursal"));

        // Verificar si hay suficiente stock
        if (sucursalInsumo.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el insumo " + insumo.getDenominacion() +
                    ": Disponible: " + sucursalInsumo.getStockActual() + ", Necesario: " + cantidad);
        }

        // Actualizar el stock
        double nuevoStock = sucursalInsumo.getStockActual() - cantidad;
        sucursalInsumo.setStockActual(nuevoStock);
    }

}
