package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.PromocionDetalle;
import com.utn.buensaborApi.repositories.base.BaseRepository;
import com.utn.buensaborApi.repositories.PromocionDetalleRepository;
import com.utn.buensaborApi.services.Implementations.base.BaseServiceImpl;
import com.utn.buensaborApi.services.Interfaces.PromocionDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PromocionDetalleServiceImpl extends BaseServiceImpl<PromocionDetalle, Long> implements PromocionDetalleService {

    @Autowired
    private PromocionDetalleRepository promocionDetalleRepository;

    public PromocionDetalleServiceImpl(BaseRepository<PromocionDetalle, Long> baseRepository) { super(baseRepository );
    }

    @Transactional
    public void eliminarDetallesLogico(List<PromocionDetalle> detalles) throws Exception{
        try {
            detalles.forEach(detalle -> {
                Optional<PromocionDetalle> detalleExistente = promocionDetalleRepository.findById(detalle.getId());
                PromocionDetalle entity = detalleExistente.get();
                entity.setFechaBaja(LocalDateTime.now());
                promocionDetalleRepository.save(entity);
            });
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
