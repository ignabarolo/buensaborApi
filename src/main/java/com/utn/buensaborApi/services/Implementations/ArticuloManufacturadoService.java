package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.dtos.Manufacturado.ArticuloManufacturadoDetalleDto;
import com.utn.buensaborApi.dtos.Manufacturado.ArticuloManufacturadoDto;
import com.utn.buensaborApi.dtos.Manufacturado.CategoriaArticuloDto;
import com.utn.buensaborApi.dtos.Manufacturado.ImagenDto;
import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.dtos.Insumo.ArticuloInsumoSimpleDto;
import com.utn.buensaborApi.repositories.*;
import com.utn.buensaborApi.mappers.ArticuloInsumoSimpleMapper;
import com.utn.buensaborApi.mappers.ArticuloManufacturadoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticuloManufacturadoService {

    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final ArticuloManufacturadoDetalleService detalleService;
    private final ImagenService imagenService;
    private final ImagenRepository imagenRepository;
    private final ArticuloInsumoService articuloInsumoService;
    private final SucursalEmpresaRepository sucursalEmpresaRepository;

    @Autowired
    private final ArticuloManufacturadoMapper mapper;

    @Autowired
    private ArticuloInsumoSimpleMapper articuloInsumoSimpleMapper;

    @Transactional(readOnly = true)
    public List<ArticuloManufacturadoDto> obtenerTodosConDetalles() {
        List<ArticuloManufacturado> articulos = articuloManufacturadoRepository.findAll();
        return articulos.stream().map(this::convertirADto).toList();
    }

    @Transactional(readOnly = true)
    public List<ArticuloManufacturadoDto> obtenerTodosActivos() {
        List<ArticuloManufacturado> articulos = articuloManufacturadoRepository.findAllByFechaBajaIsNull();
        return articulos.stream().map(this::convertirADto).toList();
    }

    public List<ArticuloManufacturadoDto> obtenerTodosSinFiltrar() {
        return articuloManufacturadoRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .toList();
    }
   
    // Buscar articulo manufacturado por id sin detalle
    @Transactional(readOnly = true)

    //public ArticuloManufacturado buscarPorIdSinDetalle(Long id) {
    public ArticuloManufacturadoDto buscarPorIdSinDetalle(Long id) {

        Optional<ArticuloManufacturado> articuloOptional = articuloManufacturadoRepository.findByIdNoDetails(id);
        ArticuloManufacturado articuloEntity = articuloOptional.orElseThrow(() ->
                new EntityNotFoundException("Artículo Manufacturado no encontrado con ID: " + id));

        articuloEntity.setDetalles(
                articuloEntity.getDetalles().stream()
                        .filter(detalle -> detalle.getFechaBaja() == null)
                        .toList()
        );
        return mapper.toDto(articuloEntity);
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

            SucursalEmpresa sucursal = sucursalEmpresaRepository.findById(1L)
                    .orElseThrow(() -> new EntityNotFoundException("Sucursal con ID 1 no encontrada"));
            articuloManufacturado.setSucursal(sucursal);

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

            detalleService.eliminarDetallesLogico(articuloExistente.getId());

            // Manejar los detalles
            if (articuloManufacturado.getDetalles() != null && !articuloManufacturado.getDetalles().isEmpty()) {
                List<ArticuloManufacturadoDetalle> detallesExistentes = articuloExistente.getDetalles();
                List<ArticuloManufacturadoDetalle> detallesIngresados = articuloManufacturado.getDetalles();

                List<ArticuloManufacturadoDetalle> nuevosDetalles = new ArrayList<>();
                List<ArticuloManufacturadoDetalle> detallesAEliminar = new ArrayList<>(detallesExistentes);

                for (ArticuloManufacturadoDetalle detalleIngresado : detallesIngresados) {
                    boolean encontrado = false;

                    for (ArticuloManufacturadoDetalle detalleExistente : detallesExistentes) {
                        if (detalleIngresado.getArticuloInsumo().getId().equals(detalleExistente.getArticuloInsumo().getId())) {
                            encontrado = true;
                            detallesAEliminar.remove(detalleExistente);
                            detalleExistente.setFechaBaja(null);

                            // Comparar cantidades
                            if (!detalleIngresado.getCantidad().equals(detalleExistente.getCantidad())) {
                                detalleExistente.setCantidad(detalleIngresado.getCantidad());
                                detalleExistente.setFechaAlta(LocalDateTime.now());
                            }
                            break;
                        }
                    }

                    if (!encontrado) {
                        detalleIngresado.setArticuloManufacturado(articuloExistente);
                        detalleIngresado.setFechaAlta(LocalDateTime.now());
                        nuevosDetalles.add(detalleIngresado);
                    }
                }

                if (!nuevosDetalles.isEmpty()) {
                    detalleService.guardarDetalles(articuloExistente, nuevosDetalles);
                }

                if (!detallesAEliminar.isEmpty()) {
                    for (ArticuloManufacturadoDetalle detalleAEliminar : detallesAEliminar) {
                        detalleService.eliminarDetallesLogico(detalleAEliminar.getId());
                    }
                }

                articuloExistente.setDetalles(detalleService.buscarDetallesPorArticuloId(articuloExistente.getId()));

                articuloExistente.costoCalculado();
                articuloExistente.precioCalculado();
            }

            // Procesar nuevas imágenes si las hay
            if (nuevasImagenes != null && !nuevasImagenes.isEmpty()) {
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

        articuloManufacturado.setFechaBaja(LocalDateTime.now());
        articuloManufacturadoRepository.save(articuloManufacturado);
        if (articuloManufacturado.getImagenes() != null && !articuloManufacturado.getImagenes().isEmpty()) {
            for (Imagen imagen : articuloManufacturado.getImagenes()) {
                    imagenService.delete(imagen.getId());
            }
        }

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

        if (articulo.getImagenes() != null && !articulo.getImagenes().isEmpty()) {
            List<ImagenDto> imagenesInfo = articulo.getImagenes().stream()
                    .map(imagen -> new ImagenDto(imagen.getId(), imagen.getNombre()))
                    .toList();
            dto.setImagenes(imagenesInfo);
        } else {
            dto.setImagenes(null);
        }

        if (articulo.getCategoria() != null) {
            CategoriaArticuloDto categoriaDto = new CategoriaArticuloDto();
            categoriaDto.setId(articulo.getCategoria().getId());
            categoriaDto.setDenominacion(articulo.getCategoria().getDenominacion());
            dto.setCategoria(categoriaDto);
        } else {
            dto.setCategoria(null);
        }
        dto.setEstado(articulo.obtenerEstado());
        dto.setFechaBaja(articulo.getFechaBaja());

        return dto;
    }

    private ArticuloManufacturadoDetalleDto convertirDetalleADto(ArticuloManufacturadoDetalle detalle) {
        ArticuloManufacturadoDetalleDto detalleDto = new ArticuloManufacturadoDetalleDto();
        detalleDto.setId(detalle.getId());
        detalleDto.setCantidad(detalle.getCantidad());

        ArticuloInsumoSimpleDto insumoSimpleDto = articuloInsumoSimpleMapper.toDto(detalle.getArticuloInsumo());
        detalleDto.setArticuloInsumo(insumoSimpleDto);

        return detalleDto;
    }


    @Transactional
    public void darDeAlta(Long id) {
        ArticuloManufacturado articulo = articuloManufacturadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));
        articulo.setFechaBaja(null);
        articuloManufacturadoRepository.save(articulo);
    }

}
