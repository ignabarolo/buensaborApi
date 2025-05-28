package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloManufacturadoDto extends BaseEntity {
    private String denominacion;
    private Double precioVenta;
    private Double margenGanancia;
    private Integer tiempoEstimadoMinutos;
    private String descripcion;
    private Double precioCosto;

    private List<ArticuloManufacturadoDetalleDto> detalles;
    private UnidadMedidaDto unidadMedida;

    private List<ImagenDto> imagenes;
    private CategoriaArticuloDto categoria;
}
