package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.repositories.base.BaseRepository;
import com.utn.buensaborApi.services.Implementations.base.BaseServiceImpl;
import com.utn.buensaborApi.services.MailService;
import com.utn.buensaborApi.enums.Estado;
import com.utn.buensaborApi.enums.TipoEnvio;
import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.dtos.Pedido.PedidoVentaDetalleDto;
import com.utn.buensaborApi.dtos.Pedido.PedidoVentaDto;

import com.utn.buensaborApi.dtos.Pedido.PromocionDetalleDto;
import com.utn.buensaborApi.dtos.Pedido.PromocionDto;
import com.utn.buensaborApi.repositories.*;
import com.utn.buensaborApi.services.Interfaces.FacturaService;
import com.utn.buensaborApi.services.Interfaces.PedidoVentaService;
import com.utn.buensaborApi.mappers.DomicilioMapper;
import com.utn.buensaborApi.mappers.PedidoVentaMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoVentaServiceImpl extends BaseServiceImpl<PedidoVenta, Long> implements PedidoVentaService {

    @Autowired
    private PedidoVentaRepository pedidoVentaRepository;


    @Autowired
    private PedidoVentaMapper mapper;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private DomicilioServices domicilioServices;

    @Autowired
    private DomicilioMapper mapperDomicilio;

    @Autowired
    private SucursalEmpresaRepository sucursalEmpresaRepository;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;


    @Autowired
    private PedidoVentaMapper pedidoVentaMapper;

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private MailService mailService;

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

    public PedidoVentaDto obtenerPedidoDtoPorId(Long id) {
        PedidoVenta pedido = pedidoVentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

        PedidoVentaDto pedidoDto = mapper.toDto(pedido);

        if (pedidoDto.getPedidosVentaDetalle() != null) {
            for (PedidoVentaDetalleDto detalle : pedidoDto.getPedidosVentaDetalle()) {
                if (detalle.getPromocion() != null && detalle.getPromocion().getId() > 0) {
                    // Buscar la promoción completa con sus detalles
                    PromocionDto promocionCompleta = getPromocionCompleta(detalle.getPromocion().getId());
                    detalle.setPromocion(promocionCompleta);
                }
            }
        }

        return pedidoDto;
    }

    public List<PedidoVentaDto> obtenerPedidosPorCliente(Long clienteId) {
        List<PedidoVenta> pedidos = pedidoVentaRepository.findByClienteId(clienteId);
        return pedidos.stream().map(mapper::toDto).toList();
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

            entity.setEstado(Estado.PENDIENTE);

            LocalDateTime fechaAhora = LocalDateTime.now();
            entity.setFechaAlta(fechaAhora);

            if (pedidoVentadto.getSucursal() != null && pedidoVentadto.getSucursal().getId() != null) {
                SucursalEmpresa sucursal = sucursalEmpresaRepository.findById(pedidoVentadto.getSucursal().getId())
                        .orElseThrow(() -> new RuntimeException("No se encontró la sucursal con ID: " + pedidoVentadto.getSucursal().getId()));
                entity.setSucursal(sucursal);
            } else {
                throw new RuntimeException("El pedido debe tener una sucursal asignada");
            }

            if (pedidoVentadto.getDomicilio() != null) {
                if (pedidoVentadto.getDomicilio().getId() != null) {
                    entity.setDomicilio(domicilioServices.obtenerPorId(pedidoVentadto.getDomicilio().getId()));
                } else {
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
                    detalle.setFechaAlta(fechaAhora);
                    detalle.setPedidoVenta(entity);
                    if (detalle.getPromocion() != null && detalle.getPromocion().getId() != null) {
                        detalle.setArticulo(null);

                        Promocion promocionActual = promocionRepository.findById(detalle.getPromocion().getId())
                                .orElseThrow(() -> new RuntimeException("No se encontró la promoción con ID: " +
                                        detalle.getPromocion().getId()));

                        detalle.setPromocion(promocionActual);

                        if (promocionActual.getPrecioVenta() == null) {
                            throw new RuntimeException("La promoción con ID " + promocionActual.getId() +
                                    " no tiene precio de venta definido");
                        }

                        detalle.setSubtotal(promocionActual.getPrecioVenta().doubleValue() * detalle.getCantidad());

                        double costoPorUnidad = 0;
                        if (promocionActual.getPromocionesDetalle() != null) {
                            for (PromocionDetalle promoDetalle : promocionActual.getPromocionesDetalle()) {
                                if (promoDetalle.getArticulo() instanceof ArticuloInsumo) {
                                    ArticuloInsumo insumo = (ArticuloInsumo) promoDetalle.getArticulo();
                                    if (insumo.getPrecioCompra() != null) {
                                        costoPorUnidad += insumo.getPrecioCompra() * promoDetalle.getCantidad();
                                    } else {
                                        throw new RuntimeException("El insumo con ID " + insumo.getId() +
                                                " no tiene precio de compra definido");
                                    }
                                } else if (promoDetalle.getArticulo() instanceof ArticuloManufacturado) {
                                    ArticuloManufacturado manufacturado = (ArticuloManufacturado) promoDetalle.getArticulo();
                                    if (manufacturado.getPrecioCosto() != null) {
                                        costoPorUnidad += manufacturado.getPrecioCosto() * promoDetalle.getCantidad();
                                    } else {
                                        throw new RuntimeException("El artículo manufacturado con ID " + manufacturado.getId() +
                                                " no tiene precio de costo definido");
                                    }
                                }
                            }
                        }
                        detalle.setSubtotalCosto(costoPorUnidad * detalle.getCantidad());

                    } else if (detalle.getArticulo() != null && detalle.getArticulo().getId() != null) {
                        detalle.setPromocion(null);
                        Long articuloId = detalle.getArticulo().getId();
                        String tipoArticulo = detalle.getArticulo().getTipoArticulo();

                        if ("insumo".equals(tipoArticulo)) {
                            Optional<ArticuloInsumo> insumoOpt = articuloInsumoRepository.findById(articuloId);
                            if (insumoOpt.isPresent()) {
                                ArticuloInsumo insumoActual = insumoOpt.get();
                                detalle.setArticulo(insumoActual);
                                if (insumoActual.getPrecioVenta() == null) {
                                    throw new RuntimeException("El insumo con ID " + insumoActual.getId() +
                                            " no tiene precio de venta definido");
                                }
                                if (insumoActual.getPrecioCompra() == null) {
                                    throw new RuntimeException("El insumo con ID " + insumoActual.getId() +
                                            " no tiene precio de compra definido");
                                }

                                detalle.setSubtotal(insumoActual.getPrecioVenta() * detalle.getCantidad());
                                detalle.setSubtotalCosto(insumoActual.getPrecioCompra() * detalle.getCantidad());
                            } else {
                                throw new RuntimeException("No se encontró el insumo con ID: " + articuloId);
                            }
                        } else if ("manufacturado".equals(tipoArticulo)) {
                            Optional<ArticuloManufacturado> manufacturadoOpt = articuloManufacturadoRepository.findById(articuloId);
                            if (manufacturadoOpt.isPresent()) {
                                ArticuloManufacturado manufacturadoActual = manufacturadoOpt.get();
                                detalle.setArticulo(manufacturadoActual);

                                if (manufacturadoActual.getPrecioVenta() == null) {
                                    throw new RuntimeException("El artículo manufacturado con ID " + manufacturadoActual.getId() +
                                            " no tiene precio de venta definido");
                                }
                                if (manufacturadoActual.getPrecioCosto() == null) {
                                    throw new RuntimeException("El artículo manufacturado con ID " + manufacturadoActual.getId() +
                                            " no tiene precio de costo definido");
                                }

                                detalle.setSubtotal(manufacturadoActual.getPrecioVenta() * detalle.getCantidad());
                                detalle.setSubtotalCosto(manufacturadoActual.getPrecioCosto() * detalle.getCantidad());
                            } else {
                                throw new RuntimeException("No se encontró el artículo manufacturado con ID: " + articuloId);
                            }
                        } else {
                            throw new RuntimeException("Tipo de artículo no válido: " + tipoArticulo);
                        }
                    } else {
                        throw new RuntimeException("El detalle del pedido debe tener un artículo o una promoción válidos");
                    }
                }
            }

            // Aplicar descuento para TAKE_AWAY (retiro en local)
            if (entity.getTipoEnvio() == TipoEnvio.TAKE_AWAY) {
                if (entity.getDescuento() != null && entity.getDescuento() < 1.0) {
                    entity.setDescuento(entity.getDescuento() * 100);
                }

                if (entity.getDescuento() == null) {
                    entity.setDescuento(10.0);
                } else {
                    entity.setDescuento(entity.getDescuento() + 10.0);
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

            if (entity.getGastoEnvio() != null && entity.getGastoEnvio() > 0) {
                totalVenta += entity.getGastoEnvio();
            }

            entity.setTotalVenta(Math.round(totalVenta * 100.0) / 100.0);
            entity.setTotalCosto(Math.round(totalCosto * 100.0) / 100.0);

            entity = pedidoVentaRepository.save(entity);

            entity.disminuirStockInsumos();

            // Crear y asociar la factura
            if (entity.getFacturas() == null || entity.getFacturas().isEmpty()) {
                Factura factura = new Factura();
                factura.setFechaFacturacion(entity.getFechaPedido());
                factura.setFormaPago(entity.getFormaPago());
                factura.setDescuento(entity.getDescuento());
                factura.setGastoEnvio(entity.getGastoEnvio());
                factura.setTotalVenta(entity.getTotalVenta());
                factura.setPedidoVenta(entity);
                factura.setFechaAlta(fechaAhora);
                factura.setCliente(entity.getCliente());
                factura.setSucursal(entity.getSucursal());
                factura = facturaService.save(factura);
                String idFacturaFormateado = String.format("%03d", factura.getId());
                String idPedidoFormateado = String.format("%05d", entity.getId());
                Integer nroComprobante = Integer.parseInt(idFacturaFormateado + idPedidoFormateado);
                factura.setNroComprobante(nroComprobante);
                facturaService.save(factura);


                List<Factura> facturas = entity.getFacturas();
                if (facturas == null) {
                    facturas = new ArrayList<>();
                }
                facturas.add(factura);
                entity.setFacturas(facturas);
            }

            return mapper.toDto(entity);
        } catch (Exception e) {
            throw new Exception("Error al guardar el pedido: " + e.getMessage(), e);
        }
    }

    // Cambiar estado de pedido de venta
    public PedidoVenta cambiarEstado(Long id, Estado nuevoEstado) {
        PedidoVenta pedido = baseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        Estado estadoAnterior = pedido.getEstado();

        pedido.setEstado(nuevoEstado);

        if (nuevoEstado == Estado.CANCELADO && nuevoEstado != estadoAnterior) {
            restaurarStockInsumos(pedido);
        }

        PedidoVenta pedidoActualizado = baseRepository.save(pedido);

        if (nuevoEstado == Estado.ENTREGADO && nuevoEstado != estadoAnterior) {
            try {
                Cliente cliente = pedido.getCliente();
                // Verificar que el cliente tenga email
                if (cliente != null && cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
                    if (pedido.getFacturas() != null && !pedido.getFacturas().isEmpty()) {
                        Factura factura = pedido.getFacturas().get(0); // Tomamos la primera factura

                        mailService.enviarFacturaEmail(factura);

                        System.out.println("Factura enviada por correo a: " + cliente.getEmail());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al enviar la factura por correo: " + e.getMessage());
            }
        }
        return pedidoActualizado;
    }

    // GET de PedidoVenta para DELIVERY
    public List<PedidoVentaDto> obtenerPedidosEnDelivery() {
        List<PedidoVenta> pedidos = pedidoVentaRepository.findByEstado(Estado.EN_DELIVERY);
        return pedidos.stream()
                .map(pedidoVentaMapper::toDto)
                .collect(Collectors.toList());
    }

    // GET de PedidoVenta para COCINERO
    public List<PedidoVentaDto> obtenerPedidosEnCocinero() {
        List<PedidoVenta> pedidos = pedidoVentaRepository.findByEstado(Estado.PREPARACION);
        int cocineros = 3;

        for (PedidoVenta pedido : pedidos) {
            pedido.setHoraEstimadaEntrega(pedido.calcularHoraEstimadaEntrega(pedidos, cocineros));
        }

        return pedidos.stream()
                .map(pedidoVentaMapper::toDto)
                .collect(Collectors.toList());
    }
    // Agregar minutos desde COCINERO
    public void agregarMinutosExtra(Long pedidoId, int minutosExtra) {
        PedidoVenta pedido = pedidoVentaRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        int minutos = pedido.getMinutosExtra() != null ? pedido.getMinutosExtra() : 0;
        pedido.setMinutosExtra(minutos + minutosExtra);
        int cocineros = 3;
        List<PedidoVenta> pedidos = pedidoVentaRepository.findByEstado(Estado.PREPARACION);
        pedido.setHoraEstimadaEntrega(pedido.calcularHoraEstimadaEntrega(pedidos, cocineros));

        pedidoVentaRepository.save(pedido);
    }

    public PedidoVenta marcarPedidoListo(Long id) {
        PedidoVenta pedido = baseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        if (pedido.getTipoEnvio() == TipoEnvio.DELIVERY) {
            pedido.setEstado(Estado.EN_DELIVERY);
        } else {
            pedido.setEstado(Estado.LISTO);
        }
        return baseRepository.save(pedido);
    }

    private PromocionDto getPromocionCompleta(int promocionId) {
        Promocion promocion = promocionRepository.findById((long) promocionId)
                .orElseThrow(() -> new EntityNotFoundException("Promoción no encontrada con ID: " + promocionId));

        PromocionDto promocionDto = new PromocionDto();
        promocionDto.setDenominacion(promocion.getDenominacion());
        promocionDto.setDescripcion(promocion.getDescripcion());
        promocionDto.setDescuento(promocion.getDescuento());

        List<PromocionDetalleDto> detallesDto = promocion.getPromocionesDetalle().stream()
                .map(detalle -> {
                    PromocionDetalleDto detalleDto = new PromocionDetalleDto();
                    detalleDto.setCantidad(detalle.getCantidad());
                    detalleDto.setArticulo(detalle.getArticulo());
                    return detalleDto;
                })
                .collect(Collectors.toList());

        promocionDto.setPromocionesDetalle(detallesDto);

        return promocionDto;
    }

    private void restaurarStockInsumos(PedidoVenta pedido) {
        Long idSucursal = pedido.getSucursal() != null ? pedido.getSucursal().getId() : 1L;

        if (pedido.getPedidosVentaDetalle() != null) {
            for (PedidoVentaDetalle detalle : pedido.getPedidosVentaDetalle()) {
                if (detalle.getArticulo() != null && detalle.getArticulo() instanceof ArticuloInsumo) {
                    ArticuloInsumo insumo = (ArticuloInsumo) detalle.getArticulo();
                    aumentarStockInsumo(insumo.getId(), idSucursal, detalle.getCantidad());
                }

                // Procesar promociones que contienen insumos
                if (detalle.getPromocion() != null) {
                    Promocion promocion = detalle.getPromocion();
                    if (promocion.getPromocionesDetalle() != null) {
                        for (PromocionDetalle promoDetalle : promocion.getPromocionesDetalle()) {
                            if (promoDetalle.getArticulo() instanceof ArticuloInsumo) {
                                ArticuloInsumo insumo = (ArticuloInsumo) promoDetalle.getArticulo();
                                int cantidadTotal = promoDetalle.getCantidad() * detalle.getCantidad();
                                aumentarStockInsumo(insumo.getId(), idSucursal, cantidadTotal);
                            } else if (promoDetalle.getArticulo() instanceof ArticuloManufacturado) {
                                ArticuloManufacturado manufacturado = (ArticuloManufacturado) promoDetalle.getArticulo();
                                if (manufacturado.getDetalles() != null) {
                                    for (ArticuloManufacturadoDetalle manuDetalle : manufacturado.getDetalles()) {
                                        if (manuDetalle.getArticuloInsumo() != null) {
                                            ArticuloInsumo insumo = manuDetalle.getArticuloInsumo();
                                            double cantidadInsumo = manuDetalle.getCantidad() * promoDetalle.getCantidad() * detalle.getCantidad();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void aumentarStockInsumo(Long insumoId, Long sucursalId, int cantidad) {
        ArticuloInsumo insumo = articuloInsumoRepository.findById(insumoId)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + insumoId));

        Optional<SucursalInsumo> sucursalInsumoOpt = insumo.getStockPorSucursal().stream()
                .filter(si -> si.getSucursal().getId().equals(sucursalId))
                .findFirst();

        if (sucursalInsumoOpt.isPresent()) {
            SucursalInsumo sucursalInsumo = sucursalInsumoOpt.get();
            sucursalInsumo.setStockActual(sucursalInsumo.getStockActual() + cantidad);
        } else {
            System.err.println("No se encontró registro de SucursalInsumo para insumo ID " + insumoId + " y sucursal ID " + sucursalId);
        }
    }
}
