package com.utn.buensaborApi.models.Dtos.Insumo;

import com.utn.buensaborApi.models.BaseEntity;
import com.utn.buensaborApi.models.Dtos.Manufacturado.UnidadMedidaDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloInsumoDto extends BaseEntity {
    private String denominacion;
    private UnidadMedidaDto unidadMedida;
}
