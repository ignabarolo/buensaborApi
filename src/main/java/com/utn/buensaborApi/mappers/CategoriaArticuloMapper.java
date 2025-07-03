package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.dtos.Manufacturado.CategoriaArticuloDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaArticuloMapper {
    CategoriaArticuloDto toDto(CategoriaArticulo entity);
    CategoriaArticulo toEntity(CategoriaArticuloDto dto);
}
