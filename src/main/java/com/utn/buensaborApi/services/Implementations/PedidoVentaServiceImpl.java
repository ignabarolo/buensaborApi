package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.Utils.ServicesUtils;
import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.models.Dtos.Pedido.PedidoVentaDto;
import com.utn.buensaborApi.models.PedidoVenta;
import com.utn.buensaborApi.models.PedidoVentaDetalle;
import com.utn.buensaborApi.repositories.BaseRepository;
import com.utn.buensaborApi.repositories.PedidoVentaRepository;
import com.utn.buensaborApi.services.Interfaces.PedidoVentaService;
import com.utn.buensaborApi.services.Mappers.PedidoVentaMapper;
import com.utn.buensaborApi.services.ClienteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    @Transactional
    public PedidoVenta save(PedidoVenta pedidoVenta) throws Exception{
        try {
            if (pedidoVenta.getFacturas() != null) {
                pedidoVenta.getFacturas().forEach(factura -> {
                    if (factura.getPedidoVenta() == null){
                        try {
                            servicesUtils.generateFactura(factura, pedidoVenta);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            if (pedidoVenta.getPedidosVentaDetalle() != null) {
                pedidoVenta.getPedidosVentaDetalle().forEach(detalle -> {
                    if (detalle.getPedidoVenta() == null){
                        detalle.setPedidoVenta(pedidoVenta);
                    }
                });
            }

            pedidoVentaRepository.save(pedidoVenta);
            return pedidoVenta;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public PedidoVentaDto saveDto(PedidoVentaDto pedidoVentadto) throws Exception {
        try {

            // Obtener cliente por ID
            Cliente cliente = clienteService.obtenerPorId(pedidoVentadto.getClienteId());
            if (cliente == null) {
                throw new Exception("Cliente no encontrado con ID: " + pedidoVentadto.getClienteId());
            }

            PedidoVenta entity = mapper.toEntity(pedidoVentadto);

            if (entity.getPedidosVentaDetalle() != null) {
                for (PedidoVentaDetalle detalle : entity.getPedidosVentaDetalle()) {
                    detalle.setPedidoVenta(entity);
                }
            }
            pedidoVentaRepository.save(entity);
            return pedidoVentadto;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
