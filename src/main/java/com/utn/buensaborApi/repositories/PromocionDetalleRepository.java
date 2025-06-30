package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Articulo;
import com.utn.buensaborApi.models.PromocionDetalle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromocionDetalleRepository  extends BaseRepository <PromocionDetalle, Long>{
    List<PromocionDetalle> findByArticulo(Articulo articulo);
}
