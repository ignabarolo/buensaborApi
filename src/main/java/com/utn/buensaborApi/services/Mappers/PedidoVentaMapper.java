package com.utn.buensaborApi.services.Mappers;

import com.utn.buensaborApi.models.Articulo;
import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloInsumoDto;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloManufacturadoDto;
import com.utn.buensaborApi.models.Dtos.Pedido.PedidoVentaDto;
import com.utn.buensaborApi.models.PedidoVenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring", uses = {FacturaMapper.class})
public interface PedidoVentaMapper {

//    PedidoVenta toEntity(PedidoVentaDto dto);
//
//    PedidoVentaDto toDto(PedidoVenta entity);

    @Mapping(source = "facturas", target = "facturas")
    PedidoVentaDto toDto(PedidoVenta entity);

    @Mapping(source = "facturas", target = "facturas")
    PedidoVenta toEntity(PedidoVentaDto dto);
//
//    // ESTO ES LO CRUCIAL:
//    // Define un metodo de mapeo para la clase base Articulo (o su DTO)
//    // y usa @SubclassMapping para indicar las subclases.
//    @SubclassMapping(source = ArticuloManufacturadoDto.class, target = ArticuloManufacturado.class)
//    @SubclassMapping(source = ArticuloInsumoDto.class, target = ArticuloInsumo.class)
//    Articulo toArticulo(ArticuloManufacturadoDto dto); // Este método manejará el mapeo polimórfico
//
//    // Y necesitas mapeos específicos para las subclases, ya que MapStruct
//    // los usará cuando la discriminación de @SubclassMapping determine el tipo.
//    ArticuloManufacturado toArticuloManufacturado(ArticuloManufacturadoDto dto);
//    ArticuloInsumo toArticuloInsumo(ArticuloInsumoDto dto);
//
//    // Asegúrate de que, si tienes ArticuloManufacturadoDetalleDto,
//    // este también esté mapeado si contiene ArticuloInsumoDto
//    // (y si necesitas mapearlo a una entidad ArticuloManufacturadoDetalle)
//    // PedidoVentaDetalle toPedidoVentaDetalle(PedidoVentaDetalleDto dto); // Si este método existe y maneja el ArticuloManufacturadoDto
}
