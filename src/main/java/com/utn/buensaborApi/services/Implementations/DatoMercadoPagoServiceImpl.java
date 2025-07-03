package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.DatoMercadoPago;
import com.utn.buensaborApi.repositories.base.BaseRepository;
import com.utn.buensaborApi.repositories.DatoMercadoPagoRopository;
import com.utn.buensaborApi.services.Implementations.base.BaseServiceImpl;
import com.utn.buensaborApi.services.Interfaces.DatoMercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatoMercadoPagoServiceImpl extends BaseServiceImpl<DatoMercadoPago, Long> implements DatoMercadoPagoService {

    @Autowired
    private DatoMercadoPagoRopository datoMercadoPagoRopository;

    public DatoMercadoPagoServiceImpl(BaseRepository<DatoMercadoPago, Long> baseRepository) { super(baseRepository );
    }
}
