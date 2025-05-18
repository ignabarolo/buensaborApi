package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.repository.BaseRepository;
import com.utn.buensaborApi.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PromocionServiceImpl extends BaseServiceImpl <Promocion, Long> implements PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    public PromocionServiceImpl(BaseRepository<Promocion, Long> baseRepository) { super(baseRepository );
    }
}
