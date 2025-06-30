package com.utn.buensaborApi.models.Dtos.Insumo;

import com.utn.buensaborApi.models.BaseEntity;
import com.utn.buensaborApi.models.Dtos.Manufacturado.CategoriaArticuloDto;
import com.utn.buensaborApi.models.Dtos.Manufacturado.SucursalInsumoDto;
import com.utn.buensaborApi.models.Dtos.Manufacturado.UnidadMedidaDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloInsumoDto extends BaseEntity {
    private String denominacion;
    private UnidadMedidaDto unidadMedida;
    private Boolean esParaElaborar;
    private Double precioCompra;
    private BigDecimal precioVenta;
    private Double margenGanancia;
    private CategoriaArticuloDto categoria;
    private List<SucursalInsumoDto> stockPorSucursal;
}
