
package com.utn.buensaborApi.dtos.Usuarios;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
public class putClienteDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private LocalDate fechaDeNacimiento;
    private domicilioDTO domicilio;  
}
