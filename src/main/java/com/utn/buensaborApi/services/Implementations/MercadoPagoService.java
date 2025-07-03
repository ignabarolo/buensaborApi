package com.utn.buensaborApi.services.Implementations;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.utn.buensaborApi.config.MercadoPagoConfiguration;
import com.utn.buensaborApi.models.DatoMercadoPago;
import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.repositories.DatoMercadoPagoRopository;
import com.utn.buensaborApi.repositories.FacturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MercadoPagoService {
    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private DatoMercadoPagoRopository datoMercadoPagoRopository;

    @Autowired
    private MercadoPagoConfiguration mercadoPagoConfig;

    public Map<String, String> crearPago(Long facturaId) throws MPException, MPApiException {
        log.info("Creando pago para factura ID: {}", facturaId);

        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + facturaId));

        // Validar monto
        Double total = factura.getTotalVenta();
        if (total == null || total <= 0) {
            throw new IllegalArgumentException("El total de la factura debe ser mayor a cero.");
        }

        // Crear items para MercadoPago (ajusta según tu modelo de detalle)
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title("Pago de Factura #" + factura.getId())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(factura.getTotalVenta()))
                .currencyId("ARS")
                .build();


        // URLs de retorno
        String successUrl = appendFacturaId(mercadoPagoConfig.getSuccessUrl(), facturaId);
        String failureUrl = appendFacturaId(mercadoPagoConfig.getFailureUrl(), facturaId);
        String pendingUrl = appendFacturaId(mercadoPagoConfig.getPendingUrl(), facturaId);

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(successUrl)
                .failure(failureUrl)
                .pending(pendingUrl)
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(item))
                .backUrls(backUrls)
                .externalReference(facturaId.toString())
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        // Guardar datos de MercadoPago en la base
        DatoMercadoPago dato = new DatoMercadoPago();
        dato.setDate_created(LocalDate.now());
        dato.setStatus("pending");
        dato.setPayment_type_id("mercadopago");
        dato.setFactura(factura);
        datoMercadoPagoRopository.save(dato);

        Map<String, String> response = new HashMap<>();
        response.put("preference_id", preference.getId());
        response.put("init_point", preference.getInitPoint());
        if (mercadoPagoConfig.isSandboxMode()) {
            response.put("sandbox_init_point", preference.getSandboxInitPoint());
        }
        return response;
    }

    private String appendFacturaId(String url, Long facturaId) {
        return url.contains("?") ? url + "&factura_id=" + facturaId : url + "?factura_id=" + facturaId;
    }

    public Map<String, String> crearPagoPorPedido(Long idPedido) throws MPException, MPApiException {
        Factura factura = obtenerFacturaPorPedido(idPedido);
        if (factura == null) {
            log.error("No se encontró factura para el pedido {}", idPedido);
            throw new IllegalArgumentException("No se encontró factura para el pedido " + idPedido);
        }
        if (factura.getPedidoVenta() == null) {
            log.error("La factura {} no tiene un pedido asociado", factura.getId());
            throw new IllegalArgumentException("La factura no tiene un pedido asociado");
        }
        return crearPago(factura.getId());
    }

    public Factura obtenerFacturaPorPedido(Long idPedido) {
        return facturaRepository.findAll().stream()
                .filter(f -> f.getPedidoVenta() != null && f.getPedidoVenta().getId().equals(idPedido))
                .findFirst()
                .orElse(null);
    }
}

