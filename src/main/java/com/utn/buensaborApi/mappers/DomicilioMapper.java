package com.utn.buensaborApi.mappers;

import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.dtos.Pedido.DomicilioDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomicilioMapper {
    Domicilio toEntity(DomicilioDto dto);
    DomicilioDto toDto(Domicilio entity);
}
