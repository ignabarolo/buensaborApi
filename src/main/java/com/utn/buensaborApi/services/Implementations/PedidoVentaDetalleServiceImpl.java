package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.PedidoVentaDetalle;
import com.utn.buensaborApi.repository.BaseRepository;
import com.utn.buensaborApi.repository.PedidoVentaDetalleRepository;
import com.utn.buensaborApi.services.Interfaces.PedidoVentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;

public class PedidoVentaDetalleServiceImpl extends BaseServiceImpl <PedidoVentaDetalle, Long> implements PedidoVentaDetalleService {

    @Autowired
    private PedidoVentaDetalleRepository pedidoVentaDetalleRepository;

    public PedidoVentaDetalleServiceImpl(BaseRepository<PedidoVentaDetalle, Long> baseRepository) { super(baseRepository );
    }
}
