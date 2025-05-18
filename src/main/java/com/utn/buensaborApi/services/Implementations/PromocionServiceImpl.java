package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.repositories.BaseRepository;
import com.utn.buensaborApi.repositories.PromocionRepository;
import com.utn.buensaborApi.services.Interfaces.PromocionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PromocionServiceImpl extends BaseServiceImpl <Promocion, Long> implements PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    public PromocionServiceImpl(BaseRepository<Promocion, Long> baseRepository) { super(baseRepository );
    }

    @Override
    public List<Promocion> findAll() throws Exception{
        try {
            List<Promocion> entities = baseRepository.findAll();
            entities.forEach(promocion -> {
                    promocion.getPromocionesDetalle().forEach(detalle ->{
                        detalle.setPromocion(null);
                    });
                    promocion.getPedidosVentaDetalle().forEach(detalle ->{
                        detalle.setPromocion(null);
                    });
            });
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Promocion findById(Long id) throws Exception{
        try {
            Optional<Promocion> entityOptional = baseRepository.findById(id);
            Promocion promocion = entityOptional.get();

            promocion.getPromocionesDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });
            promocion.getPedidosVentaDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });

            return promocion;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Promocion save(Promocion promocion) throws Exception{
        try {
            if (promocion.getPromocionesDetalle() != null) {
                promocion.getPromocionesDetalle().forEach(detalle -> {
                    if (detalle.getPromocion() == null){
                        detalle.setPromocion(promocion);
                        detalle.setFechaAlta(LocalDateTime.now());
                    }
                });
            }

            if (promocion.getPedidosVentaDetalle() != null) {
                promocion.getPedidosVentaDetalle().forEach(pedidoVenta -> {
                    if (pedidoVenta.getPromocion() == null){
                        pedidoVenta.setPromocion(promocion);
                        pedidoVenta.setFechaAlta(LocalDateTime.now());
                    }
                });
            }

            promocionRepository.save(promocion);
            return promocion;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
