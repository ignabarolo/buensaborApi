package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.Dtos.Promocion.PromocionDto;
import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.repositories.BaseRepository;
import com.utn.buensaborApi.repositories.PromocionRepository;
import com.utn.buensaborApi.services.Interfaces.PromocionService;
import com.utn.buensaborApi.services.Mappers.PromocionMapper;
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

    @Autowired
    private PromocionMapper mapper;

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

    public List<PromocionDto> findAllDto() throws Exception{
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
            var dtoList = mapper.toDtoList(entities);
            return dtoList;
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

    public PromocionDto findByIdDto(Long id) throws Exception{
        try {
            Optional<Promocion> entityOptional = baseRepository.findById(id);
            Promocion promocion = entityOptional.get();

            promocion.getPromocionesDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });
            promocion.getPedidosVentaDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });

            var dto = mapper.toDto(promocion);
            return dto;
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

    @Transactional
    public Promocion saveDto(PromocionDto dto) throws Exception{
        try {
            var promocion = mapper.toEntity(dto);

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

            promocion.setFechaAlta(LocalDateTime.now());
            promocion.setFechaModificacion(LocalDateTime.now());

            promocionRepository.save(promocion);
            return promocion;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public Promocion updateDto(Long id, PromocionDto entity) throws Exception{
        try {
            Optional<Promocion> entityOptional = baseRepository.findById(id);
            Promocion entityUpdate = entityOptional.get();

            var promocion = mapper.toEntity(entity);

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

            promocion.setFechaModificacion(LocalDateTime.now());

            entityUpdate = baseRepository.save(promocion);
            return entityUpdate;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
