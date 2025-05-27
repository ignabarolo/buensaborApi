package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.Dtos.Manufacturado.SucursalInsumoDto;
import com.utn.buensaborApi.models.SucursalInsumo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SucursalInsumoMapper {

    SucursalInsumo toEntity(SucursalInsumoDto dto);

    SucursalInsumoDto toDto(SucursalInsumo entity);

    List<SucursalInsumo> toEntityList(List<SucursalInsumoDto> dto);

    List<SucursalInsumoDto> toDtoList(List<SucursalInsumo> entity);
}
