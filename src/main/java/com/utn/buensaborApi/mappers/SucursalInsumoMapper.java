package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.dtos.Manufacturado.SucursalInsumoDto;
import com.utn.buensaborApi.models.SucursalInsumo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SucursalInsumoMapper {

    SucursalInsumoDto toDto(SucursalInsumo entity);

    SucursalInsumo toEntity(SucursalInsumoDto dto);

    List<SucursalInsumo> toEntityList(List<SucursalInsumoDto> dto);

    List<SucursalInsumoDto> toDtoList(List<SucursalInsumo> entity);
}
