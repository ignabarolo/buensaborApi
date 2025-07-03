
package com.utn.buensaborApi.dtos.Usuarios;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class domicilioDTO {
    private String calle;
    private int numero;
    private int codigoPostal;
    private Long idLocalidad; 
}
