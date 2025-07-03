package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository  extends BaseRepository<Factura, Long> {
}
