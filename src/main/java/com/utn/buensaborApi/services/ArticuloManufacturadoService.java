package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.repository.ArticuloManufacturadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
public class ArticuloManufacturadoService {

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private ArticuloManufacturadoDetalleService detalleService;

    // Buscar articulo manufacturado por id sin detalle
    @Transactional(readOnly = true)
    public ArticuloManufacturado buscarPorIdSinDetalle(Long id) {
        return articuloManufacturadoRepository.findByIdNoDetails(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Artículo Manufacturado no encontrado con ID: " + id));
    }


    //Buscar articulo manufacturado por id con detalle
    public ArticuloManufacturado buscarPorIdConDetalle(Long id) {
        return articuloManufacturadoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Artículo Manufacturado no encontrado con ID: " + id));
    }

    //Crear articulo manufacturado
    @Transactional
    public ArticuloManufacturado crear(ArticuloManufacturado articuloManufacturado) {
        articuloManufacturado.setFechaAlta(LocalDateTime.now());
        ArticuloManufacturado savedArticulo = articuloManufacturadoRepository.save(articuloManufacturado);

        if (articuloManufacturado.getDetalles() != null) {
            detalleService.guardarDetalles(savedArticulo, articuloManufacturado.getDetalles());
        }

        return savedArticulo;
    }

    //Modificar articulo manufacturado
    @Transactional
    public ArticuloManufacturado actualizar(ArticuloManufacturado articuloManufacturado) {
        if (!articuloManufacturadoRepository.existsById(articuloManufacturado.getId())) {
            throw new EntityNotFoundException
                    ("Artículo Manufacturado no encontrado con ID: " + articuloManufacturado.getId());
        }

        articuloManufacturado.setFechaModificacion(LocalDateTime.now());
        ArticuloManufacturado savedArticulo = articuloManufacturadoRepository.save(articuloManufacturado);

        if (articuloManufacturado.getDetalles() != null) {
            detalleService.actualizarDetalles(savedArticulo, articuloManufacturado.getDetalles());
        }

        return savedArticulo;
    }

    //Eliminar articulo manufacturado
    @Transactional
    public void eliminarLogico(Long id) {
        ArticuloManufacturado articuloManufacturado = articuloManufacturadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Artículo Manufacturado no encontrado con ID: " + id));

        articuloManufacturado.setFechaBaja(LocalDateTime.now());
        articuloManufacturadoRepository.save(articuloManufacturado);

        detalleService.eliminarDetallesLogico(id);
    }

}
