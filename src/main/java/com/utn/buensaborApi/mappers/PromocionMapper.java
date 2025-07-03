package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.dtos.Promocion.PromocionDto;
import com.utn.buensaborApi.models.Promocion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromocionMapper {
    PromocionDto toDto(Promocion entity);
    Promocion toEntity(PromocionDto dto);

    List<PromocionDto> toDtoList(List<Promocion> entity);
    List<Promocion> toEntityList(List<PromocionDto> dto);
}
