package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;
import com.utn.buensaborApi.models.SucursalInsumo;

import java.util.List;

public class ArticuloInsumoDto extends BaseEntity {
    private Double precioCompra;
    private Boolean esParaElaborar;
    private List<SucursalInsumoDto> stockPorSucursal;
}
