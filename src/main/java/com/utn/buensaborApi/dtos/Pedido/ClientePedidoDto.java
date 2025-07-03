package com.utn.buensaborApi.dtos.Pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientePedidoDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private DomicilioDto domicilio;
}
