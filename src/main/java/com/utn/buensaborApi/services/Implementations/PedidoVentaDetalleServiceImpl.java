package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.PedidoVentaDetalle;
import com.utn.buensaborApi.repositories.base.BaseRepository;
import com.utn.buensaborApi.repositories.PedidoVentaDetalleRepository;
import com.utn.buensaborApi.services.Implementations.base.BaseServiceImpl;
import com.utn.buensaborApi.services.Interfaces.PedidoVentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoVentaDetalleServiceImpl extends BaseServiceImpl<PedidoVentaDetalle, Long> implements PedidoVentaDetalleService {

    @Autowired
    private final PedidoVentaDetalleRepository pedidoVentaDetalleRepository;

    public PedidoVentaDetalleServiceImpl(BaseRepository<PedidoVentaDetalle, Long> baseRepository, PedidoVentaDetalleRepository pedidoVentaDetalleRepository) { super(baseRepository );
        this.pedidoVentaDetalleRepository = pedidoVentaDetalleRepository;
    }
}
