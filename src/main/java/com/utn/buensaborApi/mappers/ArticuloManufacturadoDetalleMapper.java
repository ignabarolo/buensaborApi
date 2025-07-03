package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.models.ArticuloManufacturadoDetalle;
import com.utn.buensaborApi.dtos.Manufacturado.ArticuloManufacturadoDetalleDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = { ArticuloInsumoSimpleMapper.class }
)
public interface ArticuloManufacturadoDetalleMapper {

    ArticuloManufacturadoDetalleDto toDto(ArticuloManufacturadoDetalle entity);

    ArticuloManufacturadoDetalle toEntity(ArticuloManufacturadoDetalleDto dto);
}

