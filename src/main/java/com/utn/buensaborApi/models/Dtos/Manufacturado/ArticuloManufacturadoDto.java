package com.utn.buensaborApi.models.Dtos.Manufacturado;

import com.utn.buensaborApi.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloManufacturadoDto extends BaseEntity {
    private Long id;
    private String denominacion;
    private Double precioVenta;
    private Double margenGanancia;
    private Integer tiempoEstimadoMinutos;
    private String descripcion;
    private Double precioCosto;

    private String tipoArticulo;

    private List<ArticuloManufacturadoDetalleDto> detalles;
    private UnidadMedidaDto unidadMedida;

    private List<ImagenDto> imagenes;
    private CategoriaArticuloDto categoria;
    private boolean estado;
    private LocalDateTime fechaBaja;
}
