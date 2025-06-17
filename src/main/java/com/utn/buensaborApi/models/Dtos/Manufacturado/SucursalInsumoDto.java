package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;
import com.utn.buensaborApi.models.Dtos.Insumo.ArticuloInsumoSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SucursalInsumoDto extends BaseEntity {
    private Long sucursalId;
    private ArticuloInsumoSimpleDto articuloInsumo;
    private Double stockActual;
    private Double stockMinimo;
    private Double stockMaximo;
}
