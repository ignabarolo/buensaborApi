package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.models.Dtos.Manufacturado.CategoriaArticuloDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaArticuloMapper {
    CategoriaArticuloDto toDto(CategoriaArticulo entity);
    CategoriaArticulo toEntity(CategoriaArticuloDto dto);
}
