package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.models.Dtos.Insumo.ArticuloInsumoDto;
import com.utn.buensaborApi.repositories.*;
import com.utn.buensaborApi.services.Mappers.ArticuloInsumoMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticuloInsumoService {

    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final SucursalInsumoRepository sucursalInsumoRepository;
    private final ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;
    private final PromocionRepository promocionRepository;
    private final  PromocionDetalleRepository promocionDetalleRepository;
    private final ImagenService imagenService;

    @Autowired
    private final ArticuloInsumoMapper mapper;

    @Autowired
    private SucursalEmpresaRepository sucursalEmpresaRepository;

    public List<ArticuloInsumoDto> listarTodosConDetalle() {
        return articuloInsumoRepository.findAllWithDetails().stream()
                .map(entity -> {
                    ArticuloInsumoDto dto = mapper.toDto(entity);
                    asignarUltimaImagen(entity, dto);
                    return dto;
                })
                .toList();
    }



    public List<ArticuloInsumoDto> listarTodosActivos() {
        return articuloInsumoRepository.findByEsParaElaborarFalseAndFechaBajaIsNull().stream()
                .map(mapper::toDto)
                .toList();
    }

    public ArticuloInsumo buscarPorIdConDetalle(Long id) {
        return articuloInsumoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Artículo Insumo no encontrado con ID: " + id));
    }

    public ArticuloInsumoDto buscarPorIdConDetalleSecond(Long id) {
        return articuloInsumoRepository.findByIdWithDetails(id)
                .map(entity -> {
                    ArticuloInsumoDto dto = mapper.toDto(entity);
                    asignarUltimaImagen(entity, dto);
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Artículo Insumo no encontrado con ID: " + id));
    }

    //Listar Insumos por sucursal con detalle
    public List<ArticuloInsumoDto> listarPorSucursalConDetalle(Long sucursalId) {
        return articuloInsumoRepository.findAllBySucursalWithDetails(sucursalId).stream().map(mapper::toDto).toList();
    }

    //Listar Insumos para elaborar por sucursal
    public List<ArticuloInsumo> listarParaElaborarPorSucursal(Long sucursalId) {
        return articuloInsumoRepository.findAllForElaborationBySucursal(sucursalId);
    }

    //Crear Insumo
    @Transactional
    public ArticuloInsumo crearArticuloInsumoConSucursalInsumo(ArticuloInsumo articuloInsumo, List<MultipartFile> imagenes) {
        articuloInsumo.setFechaAlta(LocalDateTime.now());

        SucursalEmpresa sucursal = sucursalEmpresaRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Sucursal 1 no encontrada"));

        articuloInsumo.setSucursal(sucursal);
        articuloInsumo.precioCalculado();

        // Guardar primero el ArticuloInsumo
        ArticuloInsumo savedArticulo = articuloInsumoRepository.save(articuloInsumo);

        // Crear y guardar SucursalInsumo asociado
        if (articuloInsumo.getStockPorSucursal() != null) {
            for (SucursalInsumo stock : articuloInsumo.getStockPorSucursal()) {
                stock.setArticuloInsumo(savedArticulo);
                stock.setSucursal(sucursal);
                stock.setFechaAlta(LocalDateTime.now());
                sucursalInsumoRepository.save(stock);
            }
        }

        if (imagenes != null && !imagenes.isEmpty()) {
            List<Imagen> imagenesGuardadas = new ArrayList<>();
            for (MultipartFile imagen : imagenes) {
                try {
                    Imagen imagenGuardada = imagenService.uploadImage(imagen);
                    imagenGuardada.setArticuloInsumo(savedArticulo);
                    imagenGuardada.setFechaAlta(LocalDateTime.now());
                    imagenGuardada.setFechaModificacion(LocalDateTime.now());
                    imagenesGuardadas.add(imagenGuardada);
                } catch (IOException e) {
                    System.err.println("Error al procesar imagen en actualización: " + e.getMessage());
                    throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
                }
            }
            savedArticulo.setImagenes(imagenesGuardadas);
            savedArticulo = articuloInsumoRepository.save(savedArticulo);
        }

        return savedArticulo;
    }


    @Transactional
    public ArticuloInsumo actualizarArticuloInsumoConSucursalInsumo(ArticuloInsumo articuloInsumo, List<MultipartFile> imagenes) {
        ArticuloInsumo articuloExistente = articuloInsumoRepository.findById(articuloInsumo.getId())
                .orElseThrow(() -> new RuntimeException("Artículo Insumo no encontrado con ID: " + articuloInsumo.getId()));

        // Guardar el precio anterior para comparación
        Double precioAnterior = articuloExistente.getPrecioCompra();

        // Crear un mapa de los stocks actuales por sucursal
        Map<Long, Double> stockActualPorSucursal = new HashMap<>();
        if (articuloExistente.getStockPorSucursal() != null) {
            for (SucursalInsumo stockExistente : articuloExistente.getStockPorSucursal()) {
                if (stockExistente.getSucursal() != null) {
                    stockActualPorSucursal.put(stockExistente.getSucursal().getId(), stockExistente.getStockActual());
                }
            }
        }

        articuloInsumo.setFechaModificacion(LocalDateTime.now());

        // Actualizar SucursalInsumo asociados manteniendo el stock actual
        if (articuloInsumo.getStockPorSucursal() != null) {
            for (SucursalInsumo stock : articuloInsumo.getStockPorSucursal()) {
                if (stock.getId() != null) {
                    stock.setFechaModificacion(LocalDateTime.now());
                } else {
                    stock.setFechaAlta(LocalDateTime.now());
                }

                stock.setArticuloInsumo(articuloExistente);

                SucursalEmpresa sucursal = sucursalEmpresaRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: 1"));
                stock.setSucursal(sucursal);

                // Mantener el stock actual si existe
                if (sucursal != null && stockActualPorSucursal.containsKey(sucursal.getId())) {
                    stock.setStockActual(stockActualPorSucursal.get(sucursal.getId()));
                }

                sucursalInsumoRepository.save(stock);
            }
        }

        // Recalcular precio de venta si es necesario
        articuloInsumo.precioCalculado();

        // Si el precio de compra cambió, actualizar todos los artículos manufacturados que usan este insumo
        if (precioAnterior != null && articuloInsumo.getPrecioCompra() != null &&
                !precioAnterior.equals(articuloInsumo.getPrecioCompra())) {
            actualizarPreciosArticulosManufacturados(articuloInsumo);
        }

        // Primero guarda el artículo para tener la entidad actualizada
        ArticuloInsumo articuloGuardado = articuloInsumoRepository.save(articuloInsumo);

        if (imagenes != null && !imagenes.isEmpty()) {
            // Guardamos cada imagen explícitamente
            for (MultipartFile imagen : imagenes) {
                try {
                    Imagen nuevaImagen = imagenService.uploadImage(imagen);
                    nuevaImagen.setArticuloInsumo(articuloGuardado);
                    nuevaImagen.setFechaAlta(LocalDateTime.now());
                    nuevaImagen.setFechaModificacion(LocalDateTime.now());
                } catch (IOException e) {
                    System.err.println("Error al procesar imagen en actualización: " + e.getMessage());
                    throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
                }
            }

            // Refrescar el artículo para obtener las imágenes actualizadas
            articuloGuardado = articuloInsumoRepository.findById(articuloGuardado.getId()).orElse(articuloGuardado);
        } else {
            // Mantener las imágenes existentes
            articuloGuardado.setImagenes(articuloExistente.getImagenes());
            articuloGuardado = articuloInsumoRepository.save(articuloGuardado);
        }

        return articuloGuardado;
    }

    @Transactional
    public void eliminarLogico(Long id) {
        ArticuloInsumo articuloInsumo = articuloInsumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo Insumo no encontrado con ID: " + id));

        LocalDateTime fechaBaja = LocalDateTime.now();
        articuloInsumo.setFechaBaja(fechaBaja);

        if (articuloInsumo.getStockPorSucursal() != null) {
            articuloInsumo.getStockPorSucursal().forEach(stock ->
                    stock.setFechaBaja(fechaBaja));
        }

        articuloInsumoRepository.save(articuloInsumo);
    }

    public List<ArticuloInsumoDto> listarTodosIncluyendoBajas() {
        return articuloInsumoRepository.findAllIncludingBajas()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    // darDeBaja en grilla
    public void darDeBaja(Long id) {
        ArticuloInsumo insumo = articuloInsumoRepository.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));
        insumo.setFechaBaja(LocalDate.now().atStartOfDay());
        articuloInsumoRepository.save(insumo);
    }
    // darDeAlta en grilla
    public void darDeAlta(Long id) {
        ArticuloInsumo insumo = articuloInsumoRepository.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));
        insumo.setFechaBaja(null);
        articuloInsumoRepository.save(insumo);
    }
    @Transactional
    public ArticuloInsumo actualizarPrecioCompra(Long id, Double nuevoPrecioCompra) {
        ArticuloInsumo articuloInsumo = articuloInsumoRepository.findByIdAndFechaBajaIsNull(id);

        if (articuloInsumo == null) {
            throw new RuntimeException("Artículo insumo no encontrado");
        }

        // Guardar el precio anterior para comparación
        Double precioAnterior = articuloInsumo.getPrecioCompra();

        // Actualizar precio de compra
        articuloInsumo.setPrecioCompra(nuevoPrecioCompra);

        // Calcular nuevo precio de venta
        articuloInsumo.precioCalculado();

        // Guardar el artículo actualizado
        articuloInsumo = articuloInsumoRepository.save(articuloInsumo);

        // Si el precio cambió, actualizar todos los artículos manufacturados que usan este insumo
        if (!precioAnterior.equals(nuevoPrecioCompra)) {
            actualizarPreciosArticulosManufacturados(articuloInsumo);
        }

        return articuloInsumo;
    }

    private void actualizarPreciosArticulosManufacturados(ArticuloInsumo insumo) {
        // Buscar todos los detalles que utilizan este insumo
        List<ArticuloManufacturadoDetalle> detalles = articuloManufacturadoDetalleRepository.findByArticuloInsumo(insumo);

        // Conjunto para evitar procesar el mismo artículo manufacturado varias veces
        Set<ArticuloManufacturado> articulosActualizados = new HashSet<>();

        for (ArticuloManufacturadoDetalle detalle : detalles) {
            ArticuloManufacturado manufacturado = detalle.getArticuloManufacturado();

            // Evitar procesar el mismo artículo varias veces
            if (manufacturado != null && !articulosActualizados.contains(manufacturado)) {
                articulosActualizados.add(manufacturado);

                // Recalcular el costo
                manufacturado.costoCalculado();

                // Recalcular el precio de venta
                manufacturado.precioCalculado();

                // Guardar los cambios
                articuloManufacturadoRepository.save(manufacturado);

                // Actualizar promociones que incluyen este artículo manufacturado
                actualizarPreciosPromociones(manufacturado);
            }
        }

        // Actualizar promociones que incluyen directamente este artículo insumo
        actualizarPreciosPromociones(insumo);
    }

    private void actualizarPreciosPromociones(Articulo articulo) {
        List<PromocionDetalle> promocionesDetalle = promocionDetalleRepository.findByArticulo(articulo);

        // Conjunto para evitar procesar la misma promoción varias veces
        Set<Promocion> promocionesActualizadas = new HashSet<>();

        for (PromocionDetalle detalle : promocionesDetalle) {
            Promocion promocion = detalle.getPromocion();

            // Evitar procesar la misma promoción varias veces
            if (promocion != null && !promocionesActualizadas.contains(promocion)) {
                promocionesActualizadas.add(promocion);

                // La promoción ya tiene metodo para calcular su precio, así que solo necesitamos guardarla
                promocionRepository.save(promocion);
            }
        }
    }
    // Metodo auxiliar para asignar la última imagen
    private void asignarUltimaImagen(ArticuloInsumo entity, ArticuloInsumoDto dto) {
        if (entity.getImagenes() != null && !entity.getImagenes().isEmpty()) {
            entity.getImagenes().stream()
                    .max(Comparator.comparing(Imagen::getId))
                    .ifPresent(ultimaImagen -> dto.setImagen(ultimaImagen.getNombre()));
        }
    }
}