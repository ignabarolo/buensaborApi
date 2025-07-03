package com.utn.buensaborApi.services.Interfaces;

import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.services.Interfaces.base.BaseService;

public interface FacturaService extends BaseService<Factura, Long> {
    Factura findById(Long id) throws Exception;
    Factura save(Factura factura) throws Exception;
    Factura anularFactura(Long facturaId) throws Exception;
    byte[] generarNotaCredito(Long facturaId) throws Exception;
}
