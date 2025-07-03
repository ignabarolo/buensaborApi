package com.utn.buensaborApi.dtos.Manufacturado;

import com.utn.buensaborApi.models.base.BaseEntity;
import com.utn.buensaborApi.dtos.Insumo.ArticuloInsumoSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloManufacturadoDetalleDto extends BaseEntity {
    private Long id;
    private Double cantidad;
    private ArticuloInsumoSimpleDto articuloInsumo;
}
