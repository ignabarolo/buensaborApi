package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloManufacturadoDetalleDto extends BaseEntity {
    private Long id;
    private Double cantidad;
    private ArticuloInsumoDto articuloInsumo;
}
