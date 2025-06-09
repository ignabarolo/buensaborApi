
package com.utn.buensaborApi.Dtos;

import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.models.Usuario;
import java.time.LocalDate;
import lombok.*;


@Getter
@Setter
public class clienteDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private LocalDate fechaDeNacimiento;
    private Usuario usuario;
    private domicilioDTO domicilio;
}
