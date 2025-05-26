package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.PedidoVenta;
import com.utn.buensaborApi.services.Implementations.PedidoVentaServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/pedidoVenta")
@Tag(name = "Pedido Venta", description = "Operaciones relacionadas con los pedidos de venta")
public class PedidoVentaController extends BaseControllerImpl<PedidoVenta, PedidoVentaServiceImpl>{
}
