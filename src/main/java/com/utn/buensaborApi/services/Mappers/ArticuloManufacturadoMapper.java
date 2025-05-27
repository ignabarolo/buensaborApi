package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloManufacturadoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticuloManufacturadoMapper {

    ArticuloManufacturado toEntity(ArticuloManufacturadoDto dto);

    ArticuloManufacturadoDto toDto(ArticuloManufacturado entity);
}
