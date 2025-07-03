package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.dtos.Insumo.ArticuloInsumoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CategoriaArticuloMapper.class, SucursalInsumoMapper.class })
public interface ArticuloInsumoMapper {
    ArticuloInsumoDto toDto(ArticuloInsumo entity);
    ArticuloInsumo toEntity(ArticuloInsumoDto dto);
}


