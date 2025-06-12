package com.utn.buensaborApi.models.Dtos.Pedido;

import com.utn.buensaborApi.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DomicilioDto extends BaseEntity {
    private String calle;
    private int numero;
    private int codigoPostal;

    private LocalidadDto localidad;
}
