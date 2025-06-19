package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.models.Dtos.ProductoVenta.FacturaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", imports = {LocalDate.class, LocalDateTime.class})
public interface FacturaMapper {

    @Mapping(target = "fechaFacturacion", source = "fechaFacturacion")
    @Mapping(target = "fechaBaja", expression = "java(entity.getFechaBaja() != null ? entity.getFechaBaja().toLocalDate() : null)")
    @Mapping(target = "nroComprobante", source = "nroComprobante")
    @Mapping(target = "totalVenta", source = "totalVenta")
    FacturaDto toDto(Factura entity);

    @Mapping(target = "fechaFacturacion", source = "fechaFacturacion")
    @Mapping(target = "fechaBaja", expression = "java(dto.getFechaBaja() != null ? LocalDateTime.of(dto.getFechaBaja(), java.time.LocalTime.MIDNIGHT) : null)")
    @Mapping(target = "nroComprobante", source = "nroComprobante")
    @Mapping(target = "totalVenta", source = "totalVenta")
    Factura toEntity(FacturaDto dto);

    List<FacturaDto> toDtoList(Set<Factura> entities);

    Set<Factura> toEntitySet(List<FacturaDto> dtos);
}