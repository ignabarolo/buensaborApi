package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.dtos.Insumo.ArticuloInsumoSimpleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticuloInsumoSimpleMapper {
    ArticuloInsumoSimpleDto toDto(ArticuloInsumo entity);
    ArticuloInsumo toEntity(ArticuloInsumoSimpleDto dto);
}
