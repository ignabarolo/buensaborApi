package com.utn.buensaborApi.dtos.Insumo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utn.buensaborApi.models.base.BaseEntity;
import com.utn.buensaborApi.dtos.Manufacturado.UnidadMedidaDto;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ArticuloInsumoSimpleDto extends BaseEntity {
    private String denominacion;
    private UnidadMedidaDto unidadMedida;
    private Boolean esParaElaborar;
    private Double precioCompra;
}

