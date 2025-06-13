package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.Utils.ServicesUtils;
import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.models.Dtos.Pedido.PedidoVentaDto;
import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.models.PedidoVenta;
import com.utn.buensaborApi.models.PedidoVentaDetalle;
import com.utn.buensaborApi.repositories.BaseRepository;
import com.utn.buensaborApi.repositories.PedidoVentaRepository;
import com.utn.buensaborApi.services.Interfaces.FacturaService;
import com.utn.buensaborApi.services.Interfaces.PedidoVentaService;
import com.utn.buensaborApi.services.Mappers.PedidoVentaMapper;
import com.utn.buensaborApi.services.ClienteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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


 //   @Override
//    @Transactional
//    public PedidoVenta save(PedidoVenta pedidoVenta) throws Exception {
//        try {
//            // Guardar primero el pedido para obtener el ID
//            pedidoVenta = pedidoVentaRepository.save(pedidoVenta);
//
//            // Crear y asociar la factura si no existe
//            if (pedidoVenta.getFacturas() == null || pedidoVenta.getFacturas().isEmpty()) {
//                Factura factura = new Factura();
//                factura.setFechaFacturacion(pedidoVenta.getFechaPedido());
//                factura.setFormaPago(pedidoVenta.getFormaPago());
//                factura.setDescuento(pedidoVenta.getDescuento());
//                factura.setGastoEnvio(pedidoVenta.getGastoEnvio());
//                factura.setTotalVenta(pedidoVenta.getTotalVenta());
//                factura.setPedidoVenta(pedidoVenta);
//
//                facturaService.save(factura);
//
//                Set<Factura> facturas = pedidoVenta.getFacturas();
//                if (facturas == null) {
//                    facturas = new HashSet<>();
//                }
//                facturas.add(factura);
//                pedidoVenta.setFacturas(facturas);
//            }
//
//            return pedidoVenta;
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }


    @Transactional
    public PedidoVentaDto saveDto(PedidoVentaDto pedidoVentadto) throws Exception {
        try {
            PedidoVenta entity = mapper.toEntity(pedidoVentadto);

            if (entity.getPedidosVentaDetalle() != null) {
                for (PedidoVentaDetalle detalle : entity.getPedidosVentaDetalle()) {
                    detalle.setPedidoVenta(entity);
                }
            }

            // Guardar primero el pedido para obtener el ID
            entity = pedidoVentaRepository.save(entity);

            // Crear y asociar la factura si no existe
            if (entity.getFacturas() == null || entity.getFacturas().isEmpty()) {
                Factura factura = new Factura();
                factura.setFechaFacturacion(entity.getFechaPedido());
                factura.setFormaPago(entity.getFormaPago());
                factura.setDescuento(entity.getDescuento());
                factura.setGastoEnvio(entity.getGastoEnvio());
                factura.setTotalVenta(entity.getTotalVenta());
                factura.setPedidoVenta(entity);

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
