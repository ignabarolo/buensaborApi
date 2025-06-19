package com.utn.buensaborApi.models.Dtos.Pedido;

import com.utn.buensaborApi.enums.Estado;
import com.utn.buensaborApi.enums.FormaPago;
import com.utn.buensaborApi.enums.TipoEnvio;
import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.models.Dtos.ProductoVenta.FacturaDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoVentaDto extends BaseEntity {
    private LocalDate fechaPedido;
    private LocalTime horaPedido;
    private Estado estado;
    private TipoEnvio tipoEnvio;
    private Double gastoEnvio;
    private FormaPago formaPago;
    private Double descuento;
    private Double totalCosto;
    private Double totalVenta;

    private Cliente cliente;

    private DomicilioDto domicilio;

    private Set<PedidoVentaDetalleDto> pedidosVentaDetalle;

    private SucursalDto sucursal;

//    private Empleado empleado;

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

    private List<FacturaDto> facturas;
}
