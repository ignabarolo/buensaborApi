package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloManufacturadoDto;
import com.utn.buensaborApi.repositories.ArticuloManufacturadoRepository;
import com.utn.buensaborApi.repositories.CategoriaArticuloRepository;
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
    private final ArticuloInsumoService articuloInsumoService;
    private final CategoriaArticuloRepository categoriaArticuloRepository;

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
    public ArticuloManufacturado crear(ArticuloManufacturado articuloManufacturado, CategoriaArticulo categoria,List<MultipartFile> imagenes) {
        try {
            //Guardar el articulo manufacturado inicialmente sin detalles ni imágenes
            articuloManufacturado.setFechaAlta(LocalDateTime.now());
            ArticuloManufacturado savedArticulo = articuloManufacturadoRepository.save(articuloManufacturado);

            //Procesar detalles
            if (articuloManufacturado.getDetalles() != null && !articuloManufacturado.getDetalles().isEmpty()) {
                List<ArticuloManufacturadoDetalle> detalles = new ArrayList<>();

                for (ArticuloManufacturadoDetalle detalle : articuloManufacturado.getDetalles()) {
                    ArticuloInsumo insumo = articuloInsumoService.buscarPorIdConDetalle(detalle.getArticuloInsumo().getId());
                    detalle.setArticuloInsumo(insumo);
                    detalle.setArticuloManufacturado(savedArticulo);
                    detalle.setFechaAlta(LocalDateTime.now());
                    detalles.add(detalle);
                }

                detalles = detalleService.guardarDetalles(savedArticulo, detalles);
                savedArticulo.setDetalles(detalles);

            }

            // Procesar Categoria
            savedArticulo.setCategoria(categoria);

            // Guardar inmediatamente después de asignar la categoría
            savedArticulo = articuloManufacturadoRepository.save(savedArticulo);


            // Procesar imágenes
            if (imagenes != null && !imagenes.isEmpty()) {
                System.out.println("Procesando " + imagenes.size() + " imágenes");
                List<Imagen> imagenesGuardadas = new ArrayList<>();

                for (MultipartFile imagen : imagenes) {
                    try {
                        Imagen imagenGuardada = imagenService.uploadImage(imagen);
                        imagenGuardada.setArticuloManufacturado(savedArticulo);
                        imagenGuardada = imagenRepository.save(imagenGuardada);
                        imagenesGuardadas.add(imagenGuardada);
                    } catch (IOException e) {
                        System.err.println("Error al procesar imagen: " + e.getMessage());
                        throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
                    }
                }

                savedArticulo.setImagenes(imagenesGuardadas);
                savedArticulo = articuloManufacturadoRepository.save(savedArticulo);
            }

            return savedArticulo;

        } catch (Exception e) {
            System.err.println("Error en la creación del artículo manufacturado: " + e.getMessage());
            throw new RuntimeException("Error al crear el artículo manufacturado: " + e.getMessage(), e);
        }
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
