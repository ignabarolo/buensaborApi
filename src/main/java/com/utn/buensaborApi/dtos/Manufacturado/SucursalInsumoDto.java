package com.utn.buensaborApi.dtos.Manufacturado;

import com.utn.buensaborApi.models.base.BaseEntity;
import com.utn.buensaborApi.dtos.Insumo.ArticuloInsumoSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SucursalInsumoDto extends BaseEntity {
//    private Long sucursalId;
    private ArticuloInsumoSimpleDto articuloInsumo;
    private Double stockActual;
    private Double stockMinimo;
    private Double stockMaximo;
}
