package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.DatoMercadoPago;
import com.utn.buensaborApi.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatoMercadoPagoRopository extends BaseRepository<DatoMercadoPago, Long> {
}
