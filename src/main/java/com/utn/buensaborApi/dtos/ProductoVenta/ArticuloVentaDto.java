package com.utn.buensaborApi.dtos.ProductoVenta;

import com.utn.buensaborApi.dtos.Manufacturado.CategoriaArticuloDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloVentaDto {
    private Long id;
    private String tipo;
    private String denominacion;
    private String descripcion;
    private CategoriaArticuloDto categoriaArticulo;
    private BigDecimal precioVenta;
    private String imagenUrl;
    private Integer stockDisponible;
    private boolean disponible;
}
