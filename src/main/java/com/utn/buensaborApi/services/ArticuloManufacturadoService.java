package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.models.Dtos.Manufacturado.*;
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
import java.util.*;
import java.util.stream.Collectors;

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
    public List<ArticuloManufacturadoDto> obtenerTodosConDetalles() {
        List<ArticuloManufacturado> articulos = articuloManufacturadoRepository.findAll()
                .stream()
                .filter(articulo -> articulo.getFechaBaja() == null) // Filtrar artículos activos
                .toList();
        return articulos.stream().map(this::convertirADto).toList();
    }
   
    // Buscar articulo manufacturado por id sin detalle
    @Transactional(readOnly = true)

    //public ArticuloManufacturado buscarPorIdSinDetalle(Long id) {
    public ArticuloManufacturadoDto buscarPorIdSinDetalle(Long id) {

        Optional<ArticuloManufacturado> articuloOptional = articuloManufacturadoRepository.findByIdNoDetails(id);
        ArticuloManufacturado articuloEntity = articuloOptional.orElseThrow(() ->
                new EntityNotFoundException("Artículo Manufacturado no encontrado con ID: " + id));

        // Filtrar los detalles con fechaBaja == null
        articuloEntity.setDetalles(
                articuloEntity.getDetalles().stream()
                        .filter(detalle -> detalle.getFechaBaja() == null)
                        .toList()
        );
        return mapper.toDto(articuloEntity);
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

                System.out.println("Calculando costo del artículo manufacturado...");
                double costoAntes = savedArticulo.getPrecioCosto() != null ? savedArticulo.getPrecioCosto() : 0.0;
                savedArticulo.costoCalculado();
                System.out.println("Costo calculado - Antes: " + costoAntes +
                        ", Después: " + savedArticulo.getPrecioCosto());

                System.out.println("Calculando precio de venta...");
                double precioAntes = savedArticulo.getPrecioVenta() != null ? savedArticulo.getPrecioVenta() : 0.0;
                savedArticulo.precioCalculado();
                System.out.println("Precio calculado - Antes: " + precioAntes +
                        ", Después: " + savedArticulo.getPrecioVenta() +
                        ", Margen aplicado: " + savedArticulo.getMargenGanancia() + "%");
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
    public ArticuloManufacturado actualizar(Long id, ArticuloManufacturado articuloManufacturado, CategoriaArticulo categoriaArticulo, List<MultipartFile> nuevasImagenes) {
        try {
            // Buscar el artículo existente
            ArticuloManufacturado articuloExistente = articuloManufacturadoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Artículo manufacturado no encontrado"));

            // Actualizar campos básicos si cambiaron
            if (!articuloExistente.getDenominacion().equals(articuloManufacturado.getDenominacion())) {
                articuloExistente.setDenominacion(articuloManufacturado.getDenominacion());
            }
            if (!articuloExistente.getDescripcion().equals(articuloManufacturado.getDescripcion())) {
                articuloExistente.setDescripcion(articuloManufacturado.getDescripcion());
            }
            if (!articuloExistente.getTiempoEstimadoMinutos().equals(articuloManufacturado.getTiempoEstimadoMinutos())) {
                articuloExistente.setTiempoEstimadoMinutos(articuloManufacturado.getTiempoEstimadoMinutos());
            }
            if (!articuloExistente.getMargenGanancia().equals(articuloManufacturado.getMargenGanancia())) {
                articuloExistente.setMargenGanancia(articuloManufacturado.getMargenGanancia());
            }
            if (!articuloExistente.getUnidadMedida().equals(articuloManufacturado.getUnidadMedida())) {
                articuloExistente.setUnidadMedida(articuloManufacturado.getUnidadMedida());
            }

            // Actualizar categoría si cambió
            if (!articuloExistente.getCategoria().equals(categoriaArticulo)) {
                articuloExistente.setCategoria(categoriaArticulo);
            }

            // Eliminar los detalles antiguos
            detalleService.eliminarDetallesLogico(articuloExistente.getId());

            // Agregar los nuevos detalles
            if (articuloManufacturado.getDetalles() != null && !articuloManufacturado.getDetalles().isEmpty()) {
                for (ArticuloManufacturadoDetalle detalle : articuloManufacturado.getDetalles()) {
                    detalle.setArticuloManufacturado(articuloExistente);
                    detalle.setFechaAlta(LocalDateTime.now());
                }
                List<ArticuloManufacturadoDetalle> detallesGuardados = detalleService.guardarDetalles(articuloExistente, articuloManufacturado.getDetalles());
                articuloExistente.setDetalles(detallesGuardados);

                // Recalcular costos y precios
                articuloExistente.costoCalculado();
                articuloExistente.precioCalculado();
            }

            // Procesar nuevas imágenes si las hay
            if (nuevasImagenes != null && !nuevasImagenes.isEmpty()) {
                // Limpiar la lista de imágenes para que Hibernate elimine las huérfanas
                articuloExistente.getImagenes().clear();

                List<Imagen> imagenesGuardadas = new ArrayList<>();
                for (MultipartFile imagen : nuevasImagenes) {
                    try {
                        Imagen imagenGuardada = imagenService.uploadImage(imagen);
                        imagenGuardada.setArticuloManufacturado(articuloExistente);
                        imagenesGuardadas.add(imagenGuardada);
                    } catch (IOException e) {
                        System.err.println("Error al procesar imagen en actualización: " + e.getMessage());
                        throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
                    }
                }
                articuloExistente.getImagenes().addAll(imagenesGuardadas);
            }


            // Guardar artículo actualizado con todo
            return articuloManufacturadoRepository.save(articuloExistente);

        } catch (Exception e) {
            System.err.println("Error en la actualización del artículo manufacturado: " + e.getMessage());
            throw new RuntimeException("Error al actualizar el artículo manufacturado: " + e.getMessage(), e);
        }
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
    private ArticuloManufacturadoDto convertirADto(ArticuloManufacturado articulo) {
        ArticuloManufacturadoDto dto = new ArticuloManufacturadoDto();
        dto.setId(articulo.getId());
        dto.setDenominacion(articulo.getDenominacion());
        dto.setPrecioVenta(articulo.getPrecioVenta());
        dto.setMargenGanancia(articulo.getMargenGanancia());
        dto.setTiempoEstimadoMinutos(articulo.getTiempoEstimadoMinutos());
        dto.setDescripcion(articulo.getDescripcion());
        dto.setPrecioCosto(articulo.getPrecioCosto());
        dto.setDetalles(articulo.getDetalles().stream()
                .map(this::convertirDetalleADto)
                .toList());

        // Asignar imágenes
        if (articulo.getImagenes() != null && !articulo.getImagenes().isEmpty()) {
            List<ImagenDto> imagenesInfo = articulo.getImagenes().stream()
                    .map(imagen -> new ImagenDto(imagen.getId(), imagen.getNombre()))
                    .toList();
            dto.setImagenes(imagenesInfo);
        } else {
            dto.setImagenes(null);
        }

        // Validar y asignar CategoriaArticulo
        if (articulo.getCategoria() != null) {
            CategoriaArticuloDto categoriaDto = new CategoriaArticuloDto();
            categoriaDto.setId(articulo.getCategoria().getId());
            categoriaDto.setDenominacion(articulo.getCategoria().getDenominacion());
            dto.setCategoria(categoriaDto);
        } else {
            dto.setCategoria(null); // O asignar un valor por defecto si es necesario
        }

        return dto;
    }

    private ArticuloManufacturadoDetalleDto convertirDetalleADto(ArticuloManufacturadoDetalle detalle) {
        ArticuloManufacturadoDetalleDto detalleDto = new ArticuloManufacturadoDetalleDto();
        detalleDto.setId(detalle.getId());
        detalleDto.setCantidad(detalle.getCantidad());

        // Usar el constructor vacío y asignar valores manualmente
        ArticuloInsumoDto articuloInsumoDto = new ArticuloInsumoDto();
        articuloInsumoDto.setId(detalle.getArticuloInsumo().getId());
        articuloInsumoDto.setDenominacion(detalle.getArticuloInsumo().getDenominacion());
        articuloInsumoDto.setPrecioCompra(detalle.getArticuloInsumo().getPrecioCompra());
        articuloInsumoDto.setEsParaElaborar(detalle.getArticuloInsumo().getEsParaElaborar());
        articuloInsumoDto.setUnidadMedida(new UnidadMedidaDto(
                detalle.getArticuloInsumo().getUnidadMedida().getId(),
                detalle.getArticuloInsumo().getUnidadMedida().getDenominacion()
        ));

        detalleDto.setArticuloInsumo(articuloInsumoDto);
        return detalleDto;
    }
}
