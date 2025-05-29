package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloManufacturadoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ArticuloManufacturadoMapper {

    ArticuloManufacturado toEntity(ArticuloManufacturadoDto dto);

    @Mapping(target = "estado", source = "entity", qualifiedByName = "mapEstado")
    ArticuloManufacturadoDto toDto(ArticuloManufacturado entity);

    @Named("mapEstado")
    default boolean mapEstado(ArticuloManufacturado entity) {
        return entity.obtenerEstado();
    }

}
