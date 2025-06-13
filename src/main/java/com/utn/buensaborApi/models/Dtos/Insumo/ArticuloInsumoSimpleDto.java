package com.utn.buensaborApi.models.Dtos.Insumo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utn.buensaborApi.models.BaseEntity;
import com.utn.buensaborApi.models.Dtos.Manufacturado.UnidadMedidaDto;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ArticuloInsumoSimpleDto extends BaseEntity {
    private String denominacion;
    private UnidadMedidaDto unidadMedida;
    private Boolean esParaElaborar;
    private Double precioCompra;
}

