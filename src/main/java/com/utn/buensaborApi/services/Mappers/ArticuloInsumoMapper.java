package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.models.Dtos.Insumo.ArticuloInsumoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { CategoriaArticuloMapper.class, SucursalInsumoMapper.class })
public interface ArticuloInsumoMapper {
    ArticuloInsumoDto toDto(ArticuloInsumo entity);
    ArticuloInsumo toEntity(ArticuloInsumoDto dto);
}


