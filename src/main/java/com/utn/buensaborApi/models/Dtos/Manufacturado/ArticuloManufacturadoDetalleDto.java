package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;

public class ArticuloManufacturadoDetalleDto extends BaseEntity {
    private Double cantidad;
    private ArticuloInsumoDto articuloInsumo;
}
