package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.models.ArticuloManufacturadoDetalle;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloManufacturadoDto;
import com.utn.buensaborApi.models.Imagen;
import com.utn.buensaborApi.models.PedidoVenta;
import com.utn.buensaborApi.repositories.ArticuloManufacturadoRepository;
import com.utn.buensaborApi.repositories.ImagenRepository;
import com.utn.buensaborApi.services.Mappers.ArticuloManufacturadoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoService {

    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final ArticuloManufacturadoDetalleService detalleService;
    private final ImagenService imagenService;
    private final ImagenRepository imagenRepository;

    @Autowired
    private final ArticuloManufacturadoMapper mapper;

    @Transactional(readOnly = true)
public List<ArticuloManufacturadoDto> obtenerTodos() {
    List<ArticuloManufacturado> articulos = articuloManufacturadoRepository.findAllNoDetails();
    return articulos.stream()
            .map(mapper::toDto)
            .toList();
}
    
    
   
    // Buscar articulo manufacturado por id sin detalle
    @Transactional(readOnly = true)

    //public ArticuloManufacturado buscarPorIdSinDetalle(Long id) {
    public ArticuloManufacturadoDto buscarPorIdSinDetalle(Long id) {

        Optional<ArticuloManufacturado> articuloOptional = articuloManufacturadoRepository.findByIdNoDetails(id);
        ArticuloManufacturado articuloEntity = articuloOptional.get();

        ArticuloManufacturadoDto articuloDto = mapper.toDto(articuloEntity);
        return articuloDto;
//        return articuloManufacturadoRepository.findByIdNoDetails(id)
//                .orElseThrow(() -> new EntityNotFoundException
//                        ("Artículo Manufacturado no encontrado con ID: " + id));
    }


    //Buscar articulo manufacturado por id con detalle
    public ArticuloManufacturado buscarPorIdConDetalle(Long id) {
        ArticuloManufacturado articuloManufacturado = articuloManufacturadoRepository.findByIdNoDetails(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Artículo Manufacturado no encontrado con ID: " + id));

        // Obtener los detalles utilizando el metodo del servicio
        List<ArticuloManufacturadoDetalle> detalles = detalleService.buscarDetallesPorArticuloId(id);
        articuloManufacturado.setDetalles(detalles);

        return articuloManufacturado;
    }


    //Crear articulo manufacturado
    @Transactional
    public ArticuloManufacturado crear(ArticuloManufacturado articuloManufacturado, List<MultipartFile> imagenes) {
        // Guardar el artículo manufacturado
        articuloManufacturado.setFechaAlta(LocalDateTime.now());
        ArticuloManufacturado savedArticulo = articuloManufacturadoRepository.save(articuloManufacturado);

        // Procesar los detalles si existen
        if (articuloManufacturado.getDetalles() != null) {
            detalleService.guardarDetalles(savedArticulo, articuloManufacturado.getDetalles());
        }

        // Procesar y guardar las imágenes si se proporcionaron
        if (imagenes != null && !imagenes.isEmpty()) {
            List<Imagen> imagenesGuardadas = new ArrayList<>();

            for (MultipartFile imagen : imagenes) {
                try {
                    // Utilizar el metodo uploadImage del ImagenService para guardar la imagen
                    Imagen imagenGuardada = imagenService.uploadImage(imagen);

                    // Establecer la relación con el artículo manufacturado
                    imagenGuardada.setArticuloManufacturado(savedArticulo);
                    imagenGuardada = imagenRepository.save(imagenGuardada);

                    imagenesGuardadas.add(imagenGuardada);
                } catch (IOException e) {
                    throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
                }
            }

            // Actualizar el artículo con las nuevas imágenes
            savedArticulo.setImagenes(imagenesGuardadas);
            savedArticulo = articuloManufacturadoRepository.save(savedArticulo);
        }
        return savedArticulo;
    }

    //Modificar articulo manufacturado
    @Transactional
    public ArticuloManufacturado actualizar(ArticuloManufacturado articuloManufacturado, List<MultipartFile> nuevasImagenes) {
        // Buscar el artículo existente
        ArticuloManufacturado articuloExistente = articuloManufacturadoRepository.findById(articuloManufacturado.getId())
                .orElseThrow(() -> new RuntimeException("Artículo manufacturado no encontrado"));

        // Actualizar campos básicos
        articuloExistente.setDenominacion(articuloManufacturado.getDenominacion());
        articuloExistente.setDescripcion(articuloManufacturado.getDescripcion());
        articuloExistente.setPrecioCosto(articuloManufacturado.getPrecioCosto());
        articuloExistente.setTiempoEstimadoMinutos(articuloManufacturado.getTiempoEstimadoMinutos());
        articuloExistente.setPrecioVenta(articuloManufacturado.getPrecioVenta());
        articuloExistente.setFechaBaja(articuloManufacturado.getFechaBaja());

        // Actualizar detalles si existen
        if (articuloManufacturado.getDetalles() != null) {
            detalleService.actualizarDetalles(articuloExistente, articuloManufacturado.getDetalles());
        }

        // Procesar nuevas imágenes si se proporcionaron
        if (nuevasImagenes != null && !nuevasImagenes.isEmpty()) {
            // Dar de baja las imágenes existentes
            if (articuloExistente.getImagenes() != null) {
                for (Imagen imagen : articuloExistente.getImagenes()) {
                    imagen.setFechaBaja(LocalDateTime.now());
                    imagenRepository.save(imagen);
                }
            }

            // Guardar nuevas imágenes
            List<Imagen> imagenesGuardadas = new ArrayList<>();
            for (MultipartFile imagen : nuevasImagenes) {
                try {
                    Imagen imagenGuardada = imagenService.uploadImage(imagen);
                    imagenGuardada.setArticuloManufacturado(articuloExistente);
                    imagenGuardada = imagenRepository.save(imagenGuardada);
                    imagenesGuardadas.add(imagenGuardada);
                } catch (IOException e) {
                    throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
                }
            }
            articuloExistente.setImagenes(imagenesGuardadas);
        }

        return articuloManufacturadoRepository.save(articuloExistente);
    }


    //Eliminar articulo manufacturado
    @Transactional
    public void eliminarLogico(Long id) {
        ArticuloManufacturado articuloManufacturado = articuloManufacturadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Artículo Manufacturado no encontrado con ID: " + id));

        // Dar de baja el artículo manufacturado
        articuloManufacturado.setFechaBaja(LocalDateTime.now());
        articuloManufacturadoRepository.save(articuloManufacturado);

        // Dar de baja las imágenes asociadas
        if (articuloManufacturado.getImagenes() != null && !articuloManufacturado.getImagenes().isEmpty()) {
            for (Imagen imagen : articuloManufacturado.getImagenes()) {
                    imagenService.delete(imagen.getId());
            }
        }

        // Dar de baja los detalles
        detalleService.eliminarDetallesLogico(id);
    }

}
