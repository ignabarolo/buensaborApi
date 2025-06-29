package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaArticuloDto {
    private Long id;
    private String denominacion;
    private Long categoriaPadreId;
    private Long sucursalId;
    private LocalDateTime fechaBaja;
    private boolean CategoriaInsumo;

    public boolean getCategoriaInsumo() {
        return CategoriaInsumo;
    }
}
