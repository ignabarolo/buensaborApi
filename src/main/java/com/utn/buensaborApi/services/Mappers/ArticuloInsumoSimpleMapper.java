package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.models.Dtos.Insumo.ArticuloInsumoSimpleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticuloInsumoSimpleMapper {
    ArticuloInsumoSimpleDto toDto(ArticuloInsumo entity);
    ArticuloInsumo toEntity(ArticuloInsumoSimpleDto dto);
}
