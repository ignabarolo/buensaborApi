package com.utn.buensaborApi.dtos.Pedido;

import com.utn.buensaborApi.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaisDto extends BaseEntity {
    private String nombre;
}
