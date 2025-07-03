package com.utn.buensaborApi.controller;

import com.utn.buensaborApi.controller.base.BaseControllerImpl;
import com.utn.buensaborApi.enums.Estado;
import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.dtos.Pedido.PedidoVentaDto;
import com.utn.buensaborApi.models.PedidoVenta;
import com.utn.buensaborApi.services.Implementations.PedidoVentaServiceImpl;
import com.utn.buensaborApi.services.Implementations.ClienteService;
import com.utn.buensaborApi.mappers.PedidoVentaMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/pedidoVenta")
@Tag(name = "Pedido Venta", description = "Operaciones relacionadas con los pedidos de venta")
public class PedidoVentaController extends BaseControllerImpl<PedidoVenta, PedidoVentaServiceImpl> {

    private static final Logger logger = LoggerFactory.getLogger(PedidoVentaController.class);

    @Autowired
    private PedidoVentaMapper pedidoVentaMapper;

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PedidoVentaServiceImpl pedidoVentaServiceImpl;

    @GetMapping("/mis-pedidos")
    public ResponseEntity<?> obtenerPedidosDelCliente(@AuthenticationPrincipal Jwt jwt) {
        try {
            // 1. Obtener el email desde el JWT
            String email = jwt.getClaimAsString("email");
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"error\": \"No se encontró el email en el token.\"}");
            }

            // 2. Buscar el cliente por email
            Cliente cliente = clienteService.obtenerPorEmail(email);
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"No se encontró un cliente con ese email.\"}");
            }

            // 3. Obtener pedidos del cliente
            List<PedidoVentaDto> pedidos = servicio.obtenerPedidosPorCliente(cliente.getId());

            return ResponseEntity.ok(pedidos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    @GetMapping("/pedido/{id}")
    public ResponseEntity<PedidoVentaDto> obtenerPedidoDtoPorId(@PathVariable Long id) {
        try {
            PedidoVentaDto pedidoDto = pedidoVentaServiceImpl.obtenerPedidoDtoPorId(id);
            return ResponseEntity.ok(pedidoDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error al obtener pedido con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/pedidos/cliente/{idCliente}")
    public ResponseEntity<List<PedidoVentaDto>> pedidosPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(pedidoVentaServiceImpl.listarPedidosDtoPorCliente(idCliente));
    }

    @GetMapping("/pedidos/cliente/{idCliente}/fechas")
    public ResponseEntity<List<PedidoVentaDto>> pedidosPorClienteYFechas(
            @PathVariable Long idCliente,
            @RequestParam("desde") String desde,
            @RequestParam("hasta") String hasta) {
        LocalDate fechaDesde = LocalDate.parse(desde);
        LocalDate fechaHasta = LocalDate.parse(hasta);
        logger.info("Consultando pedidos para cliente {} desde {} hasta {}", idCliente, fechaDesde, fechaHasta);
        return ResponseEntity.ok(pedidoVentaServiceImpl.listarPedidosDtoPorClienteYFechas(idCliente, fechaDesde, fechaHasta));
    }

    // GET PedidoVenta para DELIVERY
    @GetMapping("/delivery")
    public ResponseEntity<List<PedidoVentaDto>> getPedidosEnDelivery() {
        return ResponseEntity.ok(pedidoVentaServiceImpl.obtenerPedidosEnDelivery());
    }

    // GET PedidoVenta para COCINERO
    @GetMapping("/cocinero")
    public ResponseEntity<List<PedidoVentaDto>> getPedidosEnCocinero() {
        List<PedidoVentaDto> dtoList = pedidoVentaServiceImpl.obtenerPedidosEnCocinero();
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/Create")
    public ResponseEntity<?> save(@RequestBody PedidoVentaDto dto, @AuthenticationPrincipal Jwt jwt) {
        try {

            // 1. Obtener el email desde el JWT
            String email = jwt.getClaimAsString("email");
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"error\": \"No se encontró el email en el token.\"}");
            }

            // 2. Buscar el cliente por email
            Cliente cliente = clienteService.obtenerPorEmail(email);
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"No se encontró un cliente con ese email.\"}");
            }

            // 3. Asignar el ID del cliente al pedido
            dto.setCliente(cliente);

            // 4. Guardar el pedido
            logger.info(" pedido : {}", dto);
            return ResponseEntity.status(HttpStatus.OK).body(servicio.saveDto(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Estado nuevoEstado) {
        logger.info("Cambiando estado del pedido ID: {} al estado: {}", id, nuevoEstado);
        try {
            PedidoVenta pedidoActualizado = pedidoVentaServiceImpl.cambiarEstado(id, nuevoEstado);
            logger.info("Pedido ID: {} actualizado exitosamente al estado: {}", id, nuevoEstado);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (Exception e) {
            logger.error("Error al cambiar estado del pedido ID: {} al estado: {}. Error: {}", id, nuevoEstado, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PatchMapping("/{id}/minutos-extra")
    public ResponseEntity<Void> agregarMinutosExtra(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request
    ) {
        Integer minutos = request.get("minutosExtra");
        pedidoVentaServiceImpl.agregarMinutosExtra(id, minutos);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/marcar-listo")
    public ResponseEntity<Void> marcarPedidoListo(@PathVariable Long id) {
        pedidoVentaServiceImpl.marcarPedidoListo(id);
        return ResponseEntity.ok().build();
    }

}
