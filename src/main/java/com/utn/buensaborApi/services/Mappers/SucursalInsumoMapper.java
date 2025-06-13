package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.Dtos.Manufacturado.SucursalInsumoDto;
import com.utn.buensaborApi.models.SucursalInsumo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SucursalInsumoMapper {

    @Mapping(target = "articuloInsumo", ignore = true) // Evita mapear ArticuloInsumo dentro de SucursalInsumoDto
    @Mapping(source = "sucursal.id", target = "sucursalId")
    SucursalInsumoDto toDto(SucursalInsumo entity);

    @Mapping(target = "articuloInsumo", ignore = true)
    @Mapping(source = "sucursalId", target = "sucursal.id")
    SucursalInsumo toEntity(SucursalInsumoDto dto);

    List<SucursalInsumo> toEntityList(List<SucursalInsumoDto> dto);

    List<SucursalInsumoDto> toDtoList(List<SucursalInsumo> entity);
}
