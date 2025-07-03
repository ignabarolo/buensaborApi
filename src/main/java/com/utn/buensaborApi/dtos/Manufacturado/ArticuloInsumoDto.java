package com.utn.buensaborApi.dtos.Manufacturado;

import com.utn.buensaborApi.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticuloInsumoDto extends BaseEntity {
        private Long id;
        private String denominacion;
        private UnidadMedidaDto unidadMedida;
        private Double precioCompra;
        private Boolean esParaElaborar;
        private List<SucursalInsumoDto> stockPorSucursal;

        public ArticuloInsumoDto(String denominacion, UnidadMedidaDto unidadMedidaDto) {
        }
}
