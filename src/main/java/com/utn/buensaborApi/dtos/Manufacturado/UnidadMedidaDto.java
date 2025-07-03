package com.utn.buensaborApi.dtos.Manufacturado;

import com.utn.buensaborApi.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnidadMedidaDto extends BaseEntity {
    private Long id;
    private String denominacion;
}
