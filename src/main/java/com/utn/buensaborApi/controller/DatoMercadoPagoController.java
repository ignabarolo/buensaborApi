package com.utn.buensaborApi.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.utn.buensaborApi.controller.base.BaseControllerImpl;
import com.utn.buensaborApi.models.DatoMercadoPago;
import com.utn.buensaborApi.services.Implementations.DatoMercadoPagoServiceImpl;
import com.utn.buensaborApi.services.Implementations.MercadoPagoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/datoMercadoPago")
@Tag(name = "Mercado Pago", description = "Operaciones relacionadas con los pagos a traves de Mercado Pago")
public class DatoMercadoPagoController extends BaseControllerImpl<DatoMercadoPago, DatoMercadoPagoServiceImpl> {
    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("/{pedidoId}")
    public ResponseEntity<?> crearPagoPorPedidoCustom(@PathVariable Long pedidoId) {
        try {
            Map<String, String> response = mercadoPagoService.crearPagoPorPedido(pedidoId);
            Map<String, Object> customResponse = new HashMap<>();
            customResponse.put("preferenceId", response.get("preference_id"));
            customResponse.put("initPoint", response.get("init_point"));
            customResponse.put("sandboxInitPoint", response.get("sandbox_init_point"));
            return ResponseEntity.ok(customResponse);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear pago: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (MPException | MPApiException e) {
            log.error("Error de Mercado Pago: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al procesar el pago con Mercado Pago"));
        }
    }
}
