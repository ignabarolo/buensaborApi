package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.models.ArticuloManufacturadoDetalle;
import com.utn.buensaborApi.repositories.ArticuloManufacturadoDetalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoDetalleService {

    private final ArticuloManufacturadoDetalleRepository detalleRepository;

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
            List<ArticuloManufacturadoDetalle> nuevosDetalles) {

        // Obtener los detalles existentes
        List<ArticuloManufacturadoDetalle> detallesExistentes =
                detalleRepository.findByArticuloManufacturadoId(articuloManufacturado.getId());

        // Marcar como baja lógica los detalles que ya no están en la nueva lista
        for (ArticuloManufacturadoDetalle detalleExistente : detallesExistentes) {
            boolean sigueExistiendo = nuevosDetalles.stream()
                    .anyMatch(nuevoDetalle -> nuevoDetalle.getId() != null &&
                            nuevoDetalle.getId().equals(detalleExistente.getId()));

            if (!sigueExistiendo) {
                detalleExistente.setFechaBaja(LocalDateTime.now());
                detalleRepository.save(detalleExistente);
            }
        }

        // Actualizar o crear nuevos detalles
        for (ArticuloManufacturadoDetalle nuevoDetalle : nuevosDetalles) {
            if (nuevoDetalle.getId() != null) {
                // Buscar el detalle existente
                ArticuloManufacturadoDetalle detalleExistente = detallesExistentes.stream()
                        .filter(detalle -> detalle.getId().equals(nuevoDetalle.getId()))
                        .findFirst()
                        .orElse(null);

                if (detalleExistente != null) {
                    // Comparar y actualizar solo si hay cambios
                    if (!detalleExistente.getCantidad().equals(nuevoDetalle.getCantidad()) ||
                            !detalleExistente.getArticuloInsumo().getId().equals(nuevoDetalle.getArticuloInsumo().getId())) {
                        detalleExistente.setCantidad(nuevoDetalle.getCantidad());
                        detalleExistente.setArticuloInsumo(nuevoDetalle.getArticuloInsumo());
                        detalleExistente.setFechaModificacion(LocalDateTime.now());
                        detalleRepository.save(detalleExistente);
                    }
                }
            } else {
                // Crear nuevo detalle
                nuevoDetalle.setArticuloManufacturado(articuloManufacturado);
                nuevoDetalle.setFechaAlta(LocalDateTime.now());
                detalleRepository.save(nuevoDetalle);
            }
        }

        // Retornar la lista actualizada de detalles
        return detalleRepository.findByArticuloManufacturadoId(articuloManufacturado.getId());
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
