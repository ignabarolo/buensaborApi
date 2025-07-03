package com.utn.buensaborApi.Utils;

import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.models.PedidoVenta;
import com.utn.buensaborApi.repositories.FacturaRepository;
import com.utn.buensaborApi.repositories.PedidoVentaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServicesUtils {
    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoVentaDetalleRepository pedidoVentaDetalleRepository;

    public Factura generateFactura(Factura factura, PedidoVenta pedidoVenta) throws Exception{
        try {
            if (factura.getFacturaDetalles() != null) {
                factura.getFacturaDetalles().forEach(detalle -> {
                    if (detalle.getFactura() == null){
                        detalle.setFactura(factura);
                        detalle.setFechaAlta(LocalDateTime.now());
                    }
                });
            }

            if (factura.getDatoMercadoPago() != null) factura.getDatoMercadoPago().setFactura(factura);

            factura.setPedidoVenta(pedidoVenta);
            facturaRepository.save(factura);
            return factura;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
