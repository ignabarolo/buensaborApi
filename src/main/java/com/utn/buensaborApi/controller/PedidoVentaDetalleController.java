package com.utn.buensaborApi.controller;

import com.utn.buensaborApi.controller.base.BaseControllerImpl;
import com.utn.buensaborApi.models.DatoMercadoPago;
import com.utn.buensaborApi.services.Implementations.DatoMercadoPagoServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/pedidoVentaDetalle")
@Tag(name = "Pedido Venta Detalle", description = "Operaciones relacionadas con los detalles de los pedido de venta")
public class PedidoVentaDetalleController extends BaseControllerImpl<DatoMercadoPago, DatoMercadoPagoServiceImpl> {
}
