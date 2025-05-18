package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.PromocionDetalle;
import com.utn.buensaborApi.repositories.BaseRepository;
import com.utn.buensaborApi.repositories.PromocionDetalleRepository;
import com.utn.buensaborApi.services.Interfaces.PromocionDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromocionDetalleServiceImpl extends BaseServiceImpl <PromocionDetalle, Long> implements PromocionDetalleService {

    @Autowired
    private PromocionDetalleRepository promocionDetalleRepository;

    public PromocionDetalleServiceImpl(BaseRepository<PromocionDetalle, Long> baseRepository) { super(baseRepository );
    }
}
