package com.utn.buensaborApi.dtos.ProductoVenta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FacturaDto {
    private Long id;
    private LocalDate fechaBaja;
    private LocalDate fechaFacturacion;
    private Integer nroComprobante;
    private Double totalVenta;
}