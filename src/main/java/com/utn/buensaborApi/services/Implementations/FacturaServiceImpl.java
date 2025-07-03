package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.services.Implementations.base.BaseServiceImpl;
import com.utn.buensaborApi.services.MailService;
import com.utn.buensaborApi.services.PdfService;
import com.utn.buensaborApi.enums.Estado;
import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.repositories.base.BaseRepository;
import com.utn.buensaborApi.repositories.FacturaRepository;
import com.utn.buensaborApi.services.Interfaces.FacturaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FacturaServiceImpl extends BaseServiceImpl<Factura, Long> implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PdfService pdfService;
    @Autowired
    private MailService mailService;

    public FacturaServiceImpl(BaseRepository<Factura, Long> baseRepository) { super(baseRepository );
    }


    @Override
    public Factura findById(Long id) throws Exception {
        try {
            Optional<Factura> facturaOptional = facturaRepository.findById(id);
            return facturaOptional.orElseThrow(() ->
                    new RuntimeException("No se encontró la factura con ID: " + id));
        } catch (Exception e) {
            throw new Exception("Error al buscar factura: " + e.getMessage());
        }
    }
    @Override
    @Transactional
    public Factura save(Factura factura) throws Exception{
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

            facturaRepository.save(factura);
            return factura;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public Factura anularFactura(Long facturaId) throws Exception {
        try {
            Factura facturaOriginal = findById(facturaId);

            if (facturaOriginal == null) {
                throw new Exception("No se encontró la factura con ID: " + facturaId);
            }

            PedidoVenta pedido = facturaOriginal.getPedidoVenta();

            if (pedido == null) {
                throw new Exception("La factura no está asociada a un pedido");
            }

            // Crear nota de crédito
            Factura notaCredito = new Factura();
            notaCredito.setFechaAlta(LocalDateTime.now());
            notaCredito.setFechaFacturacion(LocalDate.now());
            notaCredito.setFormaPago(facturaOriginal.getFormaPago());
            notaCredito.setDescuento(facturaOriginal.getDescuento());
            notaCredito.setGastoEnvio(facturaOriginal.getGastoEnvio());
            notaCredito.setTotalVenta(facturaOriginal.getTotalVenta());
            notaCredito.setPedidoVenta(pedido);
            notaCredito.setCliente(facturaOriginal.getCliente());
            notaCredito.setSucursal(facturaOriginal.getSucursal());

            notaCredito.setNroComprobante(-1 * facturaOriginal.getNroComprobante());
            pedido.setEstado(Estado.CANCELADO);
            pedido.restaurarStockInsumos();
            facturaOriginal.setFechaBaja(LocalDateTime.now());
            notaCredito = save(notaCredito);
            try {
                mailService.enviarNotaCreditoEmail(notaCredito);
            } catch (Exception emailError) {
                System.err.println("Error al enviar nota de crédito por email: " + emailError.getMessage());
            }

            return notaCredito;
        } catch (Exception e) {
            throw new Exception("Error al anular la factura: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] generarNotaCredito(Long facturaId) throws Exception {
        try {
            Factura notaCredito = findById(facturaId);

            if (notaCredito.getNroComprobante() >= 0) {
                throw new Exception("El comprobante no es una nota de crédito");
            }

            return pdfService.generarNotaCreditoPdf(notaCredito);
        } catch (Exception e) {
            throw new Exception("Error al generar nota de crédito: " + e.getMessage(), e);
        }
    }

    // Metodo auxiliar para restaurar stock de insumos
    private void restaurarStockInsumos(PedidoVenta pedido) {
        if (pedido == null || pedido.getPedidosVentaDetalle() == null ||
                pedido.getSucursal() == null) {
            return;
        }

        for (PedidoVentaDetalle detalle : pedido.getPedidosVentaDetalle()) {
            Integer cantidad = detalle.getCantidad();
            if (cantidad == null || cantidad <= 0) {
                continue;
            }

            if (detalle.getArticulo() != null) {
                restaurarStockPorArticulo(detalle.getArticulo(), cantidad, pedido.getSucursal());
            }

            if (detalle.getPromocion() != null && detalle.getPromocion().getPromocionesDetalle() != null) {
                for (PromocionDetalle promoDetalle : detalle.getPromocion().getPromocionesDetalle()) {
                    if (promoDetalle.getArticulo() != null && promoDetalle.getCantidad() != null) {
                        int cantidadTotal = promoDetalle.getCantidad() * cantidad;
                        restaurarStockPorArticulo(promoDetalle.getArticulo(), cantidadTotal, pedido.getSucursal());
                    }
                }
            }
        }
    }

    private void restaurarStockPorArticulo(Articulo articulo, int cantidad, SucursalEmpresa sucursal) {
        if (articulo instanceof ArticuloInsumo) {
            restaurarStockInsumo((ArticuloInsumo) articulo, cantidad, sucursal);
        } else if (articulo instanceof ArticuloManufacturado) {
            ArticuloManufacturado manufacturado = (ArticuloManufacturado) articulo;
            if (manufacturado.getDetalles() != null) {
                for (ArticuloManufacturadoDetalle manuDetalle : manufacturado.getDetalles()) {
                    if (manuDetalle.getArticuloInsumo() != null && manuDetalle.getCantidad() != null) {
                        double cantidadInsumo = manuDetalle.getCantidad() * cantidad;
                        restaurarStockInsumo(manuDetalle.getArticuloInsumo(), cantidadInsumo, sucursal);
                    }
                }
            }
        }
    }

    private void restaurarStockInsumo(ArticuloInsumo insumo, double cantidad, SucursalEmpresa sucursal) {
        if (insumo.getStockPorSucursal() == null) {
            return;
        }
        SucursalInsumo sucursalInsumo = insumo.getStockPorSucursal().stream()
                .filter(si -> si.getSucursal().getId().equals(sucursal.getId()))
                .findFirst()
                .orElse(null);

        if (sucursalInsumo != null) {
            double nuevoStock = sucursalInsumo.getStockActual() + cantidad;
            sucursalInsumo.setStockActual(nuevoStock);
        }
    }
}
