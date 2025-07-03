package com.utn.buensaborApi.dtos.Promocion;

import com.utn.buensaborApi.models.base.BaseEntity;
import com.utn.buensaborApi.models.PromocionDetalle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromocionDto extends BaseEntity {
    private String denominacion;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Double descuento;
    private String descripcion;

    private SucursalDto sucursal;
    private List<PromocionDetalle> promocionesDetalle;
    private List<ImagenDto> imagenes;
}
