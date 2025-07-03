package com.utn.buensaborApi.dtos.Promocion;

import com.utn.buensaborApi.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImagenDto extends BaseEntity {
    private Long id;
    private String nombre;
}
