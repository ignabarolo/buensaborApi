package com.utn.buensaborApi.models.Dtos.Promocion;

import com.utn.buensaborApi.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SucursalDto extends BaseEntity {
    private Long id;
    private String nombre;
}