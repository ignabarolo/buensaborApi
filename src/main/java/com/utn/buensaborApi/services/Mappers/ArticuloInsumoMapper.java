package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.models.Dtos.Insumo.ArticuloInsumoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticuloInsumoMapper {

    ArticuloInsumo toEntity(ArticuloInsumoDto dto);

    ArticuloInsumoDto toDto(ArticuloInsumo entity);
}
