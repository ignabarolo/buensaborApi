package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.Utils.ServicesUtils;
import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.models.Dtos.Pedido.PedidoVentaDto;
import com.utn.buensaborApi.repositories.BaseRepository;
import com.utn.buensaborApi.repositories.PedidoVentaRepository;
import com.utn.buensaborApi.repositories.SucursalEmpresaRepository;
import com.utn.buensaborApi.services.DomicilioServices;
import com.utn.buensaborApi.services.Interfaces.FacturaService;
import com.utn.buensaborApi.services.Interfaces.PedidoVentaService;
import com.utn.buensaborApi.services.Mappers.DomicilioMapper;
import com.utn.buensaborApi.services.Mappers.PedidoVentaMapper;
import com.utn.buensaborApi.services.ClienteService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.itextpdf.kernel.xmp.PdfConst.Date;

@Service
public class PedidoVentaServiceImpl extends BaseServiceImpl <PedidoVenta, Long>  implements PedidoVentaService {

    @Autowired
    private PedidoVentaRepository pedidoVentaRepository;

    @Autowired
    private ServicesUtils servicesUtils;

    @Autowired
    private PedidoVentaMapper mapper;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private DomicilioServices domicilioServices;

    @Autowired
    private DomicilioMapper mapperDomicilio;

    @Autowired
    private SucursalEmpresaRepository sucursalEmpresaRepository;

    public PedidoVentaServiceImpl(BaseRepository<PedidoVenta, Long> baseRepository) { super(baseRepository );
    }

    @Override
    public List<PedidoVenta> findAll() throws Exception{
        try {
            List<PedidoVenta> entities = baseRepository.findAll();

            entities.forEach(pedidoVenta -> {
                pedidoVenta.getPedidosVentaDetalle().forEach(detalle ->{
                    detalle.setPromocion(null);
                });
            });
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PedidoVenta findById(Long id) throws Exception{
        try {
            Optional<PedidoVenta> entityOptional = baseRepository.findById(id);
            PedidoVenta pedidoVenta = entityOptional.get();

            pedidoVenta.getPedidosVentaDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });

            return pedidoVenta;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<PedidoVenta> obtenerPedidosPorCliente(Long clienteId) {
        return pedidoVentaRepository.findByClienteId(clienteId);
    }

    public List<PedidoVentaDto> listarPedidosDtoPorCliente(Long clienteId) {
        List<PedidoVenta> pedidos = pedidoVentaRepository.findByClienteId(clienteId);
        return pedidos.stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<PedidoVentaDto> listarPedidosDtoPorClienteYFechas(Long clienteId, LocalDate fechaDesde, LocalDate fechaHasta) {
        Logger logger = LoggerFactory.getLogger(PedidoVentaServiceImpl.class);
        logger.info("Consultando pedidos para cliente {} desde {} hasta {}", clienteId, fechaDesde, fechaHasta);
        List<PedidoVenta> pedidos = pedidoVentaRepository.findByClienteIdAndFechaPedidoBetween(clienteId, fechaDesde, fechaHasta);
        return pedidos.stream().map(mapper::toDto).toList();
    }

    //Crear pedido Venta, asociar domicilio y factura
    @Transactional
    public PedidoVentaDto saveDto(PedidoVentaDto pedidoVentadto) throws Exception {
        try {
            PedidoVenta entity = mapper.toEntity(pedidoVentadto);

            //Establecer fecha alta
            LocalDateTime fechaAhora = LocalDateTime.now();
            entity.setFechaAlta(fechaAhora);

            //Asignar Sucursal al pedido
            if (pedidoVentadto.getSucursal() != null && pedidoVentadto.getSucursal().getId() != null) {
                SucursalEmpresa sucursal = sucursalEmpresaRepository.findById(pedidoVentadto.getSucursal().getId())
                        .orElseThrow(() -> new RuntimeException("No se encontró la sucursal con ID: " + pedidoVentadto.getSucursal().getId()));
                entity.setSucursal(sucursal);
            } else {
                throw new RuntimeException("El pedido debe tener una sucursal asignada");
            }
            // Manejo del domicilio
            if (pedidoVentadto.getDomicilio() != null) {
                if (pedidoVentadto.getDomicilio().getId() != null) {
                    // Domicilio existente
                    entity.setDomicilio(domicilioServices.obtenerPorId(pedidoVentadto.getDomicilio().getId()));
                } else {
                    // Domicilio nuevo
                    Domicilio nuevoDomicilio = mapperDomicilio.toEntity(pedidoVentadto.getDomicilio());
                    Domicilio domicilioGuardado = domicilioServices.guardar(nuevoDomicilio);
                    entity.setDomicilio(domicilioGuardado);
                }
            } else {
                entity.setDomicilio(null);
            }

            // Procesar detalles del pedido
            if (entity.getPedidosVentaDetalle() != null) {
                for (PedidoVentaDetalle detalle : entity.getPedidosVentaDetalle()) {
                    // Establecer fecha alta para cada detalle
                    detalle.setFechaAlta(fechaAhora);

                    // Asociar el detalle con el pedido
                    detalle.setPedidoVenta(entity);

                    // Validar y establecer los campos correctamente según el tipo
                    if (detalle.getPromocion() != null && detalle.getPromocion().getId() != null) {
                        // Es una promoción - asegurar que artículo sea null
                        detalle.setArticulo(null);

                        // Calcular subtotal para la promoción
                        Promocion promocion = detalle.getPromocion();
                        detalle.setSubtotal(promocion.getPrecioVenta().doubleValue() * detalle.getCantidad());

                        // Calcular costo para inventario
                        double costoPorUnidad = 0;
                        if (promocion.getPromocionesDetalle() != null) {
                            for (PromocionDetalle promoDetalle : promocion.getPromocionesDetalle()) {
                                if (promoDetalle.getArticulo() instanceof ArticuloInsumo) {
                                    costoPorUnidad += ((ArticuloInsumo) promoDetalle.getArticulo()).getPrecioCompra() * promoDetalle.getCantidad();
                                } else if (promoDetalle.getArticulo() instanceof ArticuloManufacturado) {
                                    costoPorUnidad += ((ArticuloManufacturado) promoDetalle.getArticulo()).getPrecioCosto() * promoDetalle.getCantidad();
                                }
                            }
                        }
                        detalle.setSubtotalCosto(costoPorUnidad * detalle.getCantidad());

                    } else if (detalle.getArticulo() != null && detalle.getArticulo().getId() != null) {
                        // Es un artículo (insumo o manufacturado) - asegurar que promoción sea null
                        detalle.setPromocion(null);

                        // Calcular subtotal según tipo de artículo
                        if (detalle.getArticulo() instanceof ArticuloInsumo) {
                            ArticuloInsumo insumo = (ArticuloInsumo) detalle.getArticulo();
                            detalle.setSubtotal(insumo.getPrecioVenta() * detalle.getCantidad());
                            detalle.setSubtotalCosto(insumo.getPrecioCompra() * detalle.getCantidad());
                        } else if (detalle.getArticulo() instanceof ArticuloManufacturado) {
                            ArticuloManufacturado manufacturado = (ArticuloManufacturado) detalle.getArticulo();
                            detalle.setSubtotal(manufacturado.getPrecioVenta() * detalle.getCantidad());
                            detalle.setSubtotalCosto(manufacturado.getPrecioCosto() * detalle.getCantidad());
                        }
                    } else {
                        // Si ambos son null o inválidos, es un error
                        throw new RuntimeException("El detalle del pedido debe tener un artículo o una promoción válidos");
                    }
                }
            }
            // Calcular totales del pedido
            double totalVenta = 0;
            double totalCosto = 0;
            if (entity.getPedidosVentaDetalle() != null) {
                for (PedidoVentaDetalle detalle : entity.getPedidosVentaDetalle()) {
                    totalVenta += detalle.getSubtotal();
                    totalCosto += detalle.getSubtotalCosto();
                }
            }

            // Aplicar descuento si existe
            if (entity.getDescuento() != null && entity.getDescuento() > 0) {
                double montoDescuento = totalVenta * (entity.getDescuento() / 100.0);
                totalVenta -= montoDescuento;
            }

            // Agregar costo de envío si existe
            if (entity.getGastoEnvio() != null && entity.getGastoEnvio() > 0) {
                totalVenta += entity.getGastoEnvio();
            }

            entity.setTotalVenta(Math.round(totalVenta * 100.0) / 100.0);  // Redondear a 2 decimales
            entity.setTotalCosto(Math.round(totalCosto * 100.0) / 100.0);


            // Guardar primero el pedido para obtener el ID
            entity = pedidoVentaRepository.save(entity);

            // Actualizar el stock de insumos
            entity.disminuirStockInsumos();

            // Crear y asociar la factura si no existe
            if (entity.getFacturas() == null || entity.getFacturas().isEmpty()) {
                Factura factura = new Factura();
                factura.setFechaFacturacion(entity.getFechaPedido());
                factura.setFormaPago(entity.getFormaPago());
                factura.setDescuento(entity.getDescuento());
                factura.setGastoEnvio(entity.getGastoEnvio());
                factura.setTotalVenta(entity.getTotalVenta());
                factura.setPedidoVenta(entity);
                factura.setFechaAlta(fechaAhora);

                facturaService.save(factura);

                Set<Factura> facturas = entity.getFacturas();
                if (facturas == null) {
                    facturas = new HashSet<>();
                }
                facturas.add(factura);
                entity.setFacturas(facturas);
            }

            return mapper.toDto(entity);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
