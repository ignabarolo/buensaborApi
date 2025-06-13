package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.ArticuloManufacturadoDetalle;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloManufacturadoDetalleDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = { ArticuloInsumoSimpleMapper.class } // para mapear articuloInsumo simple en los detalles
)
public interface ArticuloManufacturadoDetalleMapper {

    ArticuloManufacturadoDetalleDto toDto(ArticuloManufacturadoDetalle entity);

    ArticuloManufacturadoDetalle toEntity(ArticuloManufacturadoDetalleDto dto);
}

