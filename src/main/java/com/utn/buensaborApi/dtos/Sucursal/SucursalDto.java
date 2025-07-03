package com.utn.buensaborApi.dtos.Sucursal;

import com.utn.buensaborApi.dtos.Pedido.DomicilioDto;
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

