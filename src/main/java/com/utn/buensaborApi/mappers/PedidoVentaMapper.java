package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.dtos.Pedido.PedidoVentaDto;
import com.utn.buensaborApi.models.PedidoVenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FacturaMapper.class})
public interface PedidoVentaMapper {

    @Mapping(source = "horaEstimadaEntrega", target = "horaEstimadaEntrega")
    @Mapping(source = "minutosExtra", target = "minutosExtra")
    @Mapping(source = "facturas", target = "facturas")
    PedidoVentaDto toDto(PedidoVenta entity);

    @Mapping(source = "horaEstimadaEntrega", target = "horaEstimadaEntrega")
    @Mapping(source = "minutosExtra", target = "minutosExtra")
    @Mapping(source = "facturas", target = "facturas")
    PedidoVenta toEntity(PedidoVentaDto dto);

}
