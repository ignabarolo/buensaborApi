package com.utn.buensaborApi.models.Dtos.Sucursal;

import com.utn.buensaborApi.models.Dtos.Pedido.DomicilioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SucursalDto {
    private Long id;
    private String nombre;
    private String horaApertura;
    private String horaCierre;
    private DomicilioDto domicilioDto;
}

