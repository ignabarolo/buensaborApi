package com.utn.buensaborApi.Controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.utn.buensaborApi.models.DatoMercadoPago;
import com.utn.buensaborApi.services.Implementations.DatoMercadoPagoServiceImpl;
import com.utn.buensaborApi.services.MercadoPagoService;
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
public class DatoMercadoPagoController extends BaseControllerImpl<DatoMercadoPago, DatoMercadoPagoServiceImpl>{
    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("/{facturaId}")
    public ResponseEntity<?> crearPago(@PathVariable Long facturaId) {
        try {
            Map<String, String> datosPago = mercadoPagoService.crearPago(facturaId);

            Map<String, String> response = new HashMap<>();
            response.put("preferenceId", datosPago.get("preference_id"));
            response.put("initPoint", datosPago.get("init_point"));
            response.put("sandboxInitPoint", datosPago.getOrDefault("sandbox_init_point", null));

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear pago: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (MPException | MPApiException e) {
            log.error("Error de Mercado Pago: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al procesar el pago con Mercado Pago"));
        }
    }

}
