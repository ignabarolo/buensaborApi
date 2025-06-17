package com.utn.buensaborApi.Dtos;

import com.utn.buensaborApi.enums.Rol;
import com.utn.buensaborApi.Dtos.domicilioDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpleadoDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private Rol rol;
    private Long sucursalId;
    private domicilioDTO domicilio;
    
    private String password;  // <-- agregá esta línea
}

