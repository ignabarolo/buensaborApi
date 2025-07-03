package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.dtos.Manufacturado.ArticuloManufacturadoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = { ArticuloManufacturadoDetalleMapper.class }
)
public interface ArticuloManufacturadoMapper {

    ArticuloManufacturado toEntity(ArticuloManufacturadoDto dto);

    @Mapping(target = "estado", source = "entity", qualifiedByName = "mapEstado")
    ArticuloManufacturadoDto toDto(ArticuloManufacturado entity);

    @Named("mapEstado")
    default boolean mapEstado(ArticuloManufacturado entity) {
        return entity.obtenerEstado();
    }

}
