package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Dtos.Pedido.PedidoVentaDto;
import com.utn.buensaborApi.models.PedidoVenta;
import com.utn.buensaborApi.services.Implementations.PedidoVentaServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/pedidoVenta")
@Tag(name = "Pedido Venta", description = "Operaciones relacionadas con los pedidos de venta")
public class PedidoVentaController extends BaseControllerImpl<PedidoVenta, PedidoVentaServiceImpl>{

    @PostMapping("/Create")
    public ResponseEntity<?> save(@RequestBody PedidoVentaDto dto){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.saveDto(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":" + "\"" + e.getMessage() + ".\"}");
        }
    }
}
