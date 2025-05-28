package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SucursalInsumoDto extends BaseEntity {
    private Double stockActual;
    private Double stockMinimo;
    private Double stockMaximo;
}
