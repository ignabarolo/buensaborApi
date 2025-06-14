package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.PedidoVenta;

import java.util.List;

public interface PedidoVentaRepository  extends BaseRepository <PedidoVenta, Long>{

    List<PedidoVenta> findByClienteId(Long clienteId);

}
