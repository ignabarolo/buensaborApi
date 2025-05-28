package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloInsumoDto extends BaseEntity {
    private Double precioCompra;
    private Boolean esParaElaborar;
    private List<SucursalInsumoDto> stockPorSucursal;
}
