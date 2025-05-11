package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.models.ArticuloManufacturadoDetalle;
import com.utn.buensaborApi.repository.ArticuloManufacturadoDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ArticuloManufacturadoDetalleService {

    @Autowired
    private ArticuloManufacturadoDetalleRepository detalleRepository;

    //Guardar detalles de articulo manufacturado
    @Transactional
    public List<ArticuloManufacturadoDetalle> guardarDetalles(
            ArticuloManufacturado articuloManufacturado,
            List<ArticuloManufacturadoDetalle> detalles) {

        detalles.forEach(detalle -> {
            detalle.setArticuloManufacturado(articuloManufacturado);
            detalle.setFechaAlta(LocalDateTime.now());
        });

        return detalleRepository.saveAll(detalles);
    }

    //Modificar detalles de articulo manufacturado
    @Transactional
    public List<ArticuloManufacturadoDetalle> actualizarDetalles(
            ArticuloManufacturado articuloManufacturado,
            List<ArticuloManufacturadoDetalle> detalles) {

        // Eliminar detalles existentes
        List<ArticuloManufacturadoDetalle> detallesExistentes =
                detalleRepository.findByArticuloManufacturadoId(articuloManufacturado.getId());

        detallesExistentes.forEach(detalle -> {
            if (!detalles.contains(detalle)) {
                detalle.setFechaBaja(LocalDateTime.now());
                detalleRepository.save(detalle);
            }
        });

        // Actualizar o crear nuevos detalles
        detalles.forEach(detalle -> {
            detalle.setArticuloManufacturado(articuloManufacturado);
            if (detalle.getId() != null) {
                detalle.setFechaModificacion(LocalDateTime.now());
            } else {
                detalle.setFechaAlta(LocalDateTime.now());
            }
        });

        return detalleRepository.saveAll(detalles);
    }

    //Eliminar detalles de articulo manufacturado
    @Transactional
    public void eliminarDetallesLogico(Long articuloManufacturadoId) {
        List<ArticuloManufacturadoDetalle> detalles =
                detalleRepository.findByArticuloManufacturadoId(articuloManufacturadoId);

        if (!detalles.isEmpty()) {
            LocalDateTime fechaBaja = LocalDateTime.now();
            detalles.forEach(detalle -> detalle.setFechaBaja(fechaBaja));
            detalleRepository.saveAll(detalles);
        }
    }

    //Buscar detalles de un articulo manufacturado por id
    public List<ArticuloManufacturadoDetalle> buscarDetallesPorArticuloId(Long articuloId) {
        return detalleRepository.findByArticuloManufacturadoId(articuloId);
    }
}
