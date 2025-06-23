package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.models.Dtos.Promocion.PromocionDto;
import com.utn.buensaborApi.repositories.ArticuloManufacturadoRepository;
import com.utn.buensaborApi.repositories.BaseRepository;
import com.utn.buensaborApi.repositories.ImagenRepository;
import com.utn.buensaborApi.repositories.PromocionRepository;
import com.utn.buensaborApi.services.ImagenService;
import com.utn.buensaborApi.services.Interfaces.PromocionService;
import com.utn.buensaborApi.services.Mappers.PromocionMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromocionServiceImpl extends BaseServiceImpl <Promocion, Long> implements PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private PromocionMapper mapper;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private PromocionDetalleServiceImpl detalleService;


    public PromocionServiceImpl(BaseRepository<Promocion, Long> baseRepository) { super(baseRepository );
    }

    @Override
    public List<Promocion> findAll() throws Exception{
        try {
            List<Promocion> entities = baseRepository.findAll();
            entities.forEach(promocion -> {
                    promocion.getPromocionesDetalle().forEach(detalle ->{
                        detalle.setPromocion(null);
                    });
                    promocion.getPedidosVentaDetalle().forEach(detalle ->{
                        detalle.setPromocion(null);
                    });
            });
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<PromocionDto> findAllDto() throws Exception{
        try {
            List<Promocion> entities = baseRepository.findAll();
            entities.forEach(promocion -> {
                    promocion.getPromocionesDetalle().forEach(detalle ->{
                        detalle.setPromocion(null);
                    });
                    promocion.getPedidosVentaDetalle().forEach(detalle ->{
                        detalle.setPromocion(null);
                    });
            });
            var dtoList = mapper.toDtoList(entities);
            return dtoList;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Promocion findById(Long id) throws Exception{
        try {
            Optional<Promocion> entityOptional = baseRepository.findById(id);
            Promocion promocion = entityOptional.get();

            promocion.getPromocionesDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });
            promocion.getPedidosVentaDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });

            return promocion;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public PromocionDto findByIdDto(Long id) throws Exception{
        try {
            Optional<Promocion> entityOptional = baseRepository.findById(id);
            Promocion promocion = entityOptional.get();

            promocion.getPromocionesDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });
            promocion.getPedidosVentaDetalle().forEach(detalle ->{
                detalle.setPromocion(null);
            });

            var dto = mapper.toDto(promocion);
            return dto;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Promocion save(Promocion promocion) throws Exception{
        try {
            if (promocion.getPromocionesDetalle() != null) {
                promocion.getPromocionesDetalle().forEach(detalle -> {
                    if (detalle.getPromocion() == null){
                        detalle.setPromocion(promocion);
                        detalle.setFechaAlta(LocalDateTime.now());
                    }
                });
            }

            if (promocion.getPedidosVentaDetalle() != null) {
                promocion.getPedidosVentaDetalle().forEach(pedidoVenta -> {
                    if (pedidoVenta.getPromocion() == null){
                        pedidoVenta.setPromocion(promocion);
                        pedidoVenta.setFechaAlta(LocalDateTime.now());
                    }
                });
            }

            promocionRepository.save(promocion);
            return promocion;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public Promocion saveDto(PromocionDto dto, List<MultipartFile> imagenes) throws Exception{
        try {
            var promocion = mapper.toEntity(dto);

            if (promocion.getPromocionesDetalle() != null) {
                promocion.getPromocionesDetalle().forEach(detalle -> {
                    if (detalle.getPromocion() == null){
                        detalle.setPromocion(promocion);
                        detalle.setFechaAlta(LocalDateTime.now());
                    }
                });
            }

            if (promocion.getPedidosVentaDetalle() != null) {
                promocion.getPedidosVentaDetalle().forEach(pedidoVenta -> {
                    if (pedidoVenta.getPromocion() == null){
                        pedidoVenta.setPromocion(promocion);
                        pedidoVenta.setFechaAlta(LocalDateTime.now());
                    }
                });
            }

            var sucursal = new SucursalEmpresa();
            sucursal.setId(1L);
            sucursal.setNombre("Sucursal Luján");

            promocion.setSucursal(sucursal);

            promocion.setFechaAlta(LocalDateTime.now());
            promocion.setFechaModificacion(LocalDateTime.now());

            var savedPromocion = promocionRepository.save(promocion);

            // Procesar imágenes
            if (imagenes != null && !imagenes.isEmpty()) {
                System.out.println("Procesando " + imagenes.size() + " imágenes");
                List<Imagen> imagenesGuardadas = new ArrayList<>();

                for (MultipartFile imagen : imagenes) {
                    try {
                        Imagen imagenGuardada = imagenService.uploadImage(imagen);
                        imagenGuardada.setPromocion(savedPromocion);
                        imagenGuardada.setFechaAlta(LocalDateTime.now());
                        imagenGuardada.setFechaModificacion(LocalDateTime.now());
                        imagenGuardada = imagenRepository.save(imagenGuardada);
                        imagenesGuardadas.add(imagenGuardada);
                    } catch (IOException e) {
                        System.err.println("Error al procesar imagen: " + e.getMessage());
                        throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
                    }
                }

                savedPromocion.setImagenes(imagenesGuardadas);
                savedPromocion = promocionRepository.save(savedPromocion);
            }



            return savedPromocion;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public Promocion updateDto(Long id, PromocionDto entity, List<MultipartFile> imagenes) throws Exception{
        try {

            Optional<Promocion> entityOptional = baseRepository.findById(id);
            Promocion promocionExistente = entityOptional.get();

            var promocion = mapper.toEntity(entity);

//            if (promocion.getPromocionesDetalle() != null && !promocion.getPromocionesDetalle().isEmpty()) {
//                List<PromocionDetalle> detallesExistentes = promocionExistente.getPromocionesDetalle();
//                List<PromocionDetalle> detallesIngresados = promocion.getPromocionesDetalle();
//
//                // Crear listas para nuevos detalles y detalles a eliminar
//                List<PromocionDetalle> nuevosDetalles = new ArrayList<>();
//                List<PromocionDetalle> detallesAEliminar = new ArrayList<>(detallesExistentes);
//
//                for (PromocionDetalle detalleIngresado : detallesIngresados) {
//                    boolean encontrado = false;
//
//                    for (PromocionDetalle detalleExistente : detallesExistentes) {
//                        if (detalleIngresado.getArticulo().getId().equals(detalleExistente.getArticulo().getId())) {
//                            encontrado = true;
//                            detallesAEliminar.remove(detalleExistente);
//                            detalleExistente.setFechaBaja(null);
//
//                            // Comparar cantidades
//                            if (!detalleIngresado.getCantidad().equals(detalleExistente.getCantidad())) {
//                                detalleExistente.setCantidad(detalleIngresado.getCantidad());
//                                detalleExistente.setFechaAlta(LocalDateTime.now());
//                            }
//                            break;
//                        }
//                    }

                    // Si no se encontró, es un nuevo detalle
//                    if (!encontrado) {
//                        detalleIngresado.setArticulo(articuloExistente);
//                        detalleIngresado.setFechaAlta(LocalDateTime.now());
//                        nuevosDetalles.add(detalleIngresado);
//                    }
//                }

//                // Guardar nuevos detalles
//                if (!nuevosDetalles.isEmpty()) {
//                    detalleService.guardarDetalles(articuloExistente, nuevosDetalles);
//                }

//                // Eliminar detalles que no están en los ingresados
//                if (!detallesAEliminar.isEmpty()) {
//                    for (ArticuloManufacturadoDetalle detalleAEliminar : detallesAEliminar) {
//                        detalleService.eliminarDetallesLogico(detalleAEliminar.getId());
//                    }
//                }

                // Actualizar la lista de detalles en el artículo existente
//                articuloExistente.setDetalles(detalleService.buscarDetallesPorArticuloId(articuloExistente.getId()));

                // Recalcular costos y precios
//                articuloExistente.costoCalculado();
//                articuloExistente.precioCalculado();
//            }
//
//            if (promocion.getImagenes() != null){
//                promocion.getImagenes().forEach(imagen ->
//                        imagen.setFechaModificacion(LocalDateTime.now()));
//            }

            if (promocion.getPromocionesDetalle() != null) {
                promocion.getPromocionesDetalle().forEach(detalle -> {
                    if (detalle.getPromocion() == null){
                        detalle.setPromocion(promocion);
                        detalle.setFechaModificacion(LocalDateTime.now());
                    }
                });
            }

            if (promocion.getPedidosVentaDetalle() != null) {
                promocion.getPedidosVentaDetalle().forEach(pedidoVenta -> {
                    if (pedidoVenta.getPromocion() == null){
                        pedidoVenta.setPromocion(promocion);
                        pedidoVenta.setFechaModificacion(LocalDateTime.now());
                    }
                });
            }

            promocion.setFechaModificacion(LocalDateTime.now());
            var savedPromocion = promocionRepository.save(promocion);

            if (imagenes != null && !imagenes.isEmpty()) {
                // Limpiar la lista de imágenes para que Hibernate elimine las huérfanas
                promocionExistente.getImagenes().clear();

                List<Imagen> imagenesGuardadas = new ArrayList<>();
                for (MultipartFile imagen : imagenes) {
                    try {
                        Imagen imagenGuardada = imagenService.uploadImage(imagen);
                        imagenGuardada.setPromocion(promocionExistente);
                        imagenGuardada.setFechaAlta(LocalDateTime.now());
                        imagenGuardada.setFechaModificacion(LocalDateTime.now());
                        imagenesGuardadas.add(imagenGuardada);
                    } catch (IOException e) {
                        System.err.println("Error al procesar imagen en actualización: " + e.getMessage());
                        throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
                    }
                }
                promocionExistente.getImagenes().addAll(imagenesGuardadas);
            }



            return savedPromocion;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void eliminarLogico(Long id) throws Exception{
        try {
            Promocion promocion = promocionRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException
                            ("Promocion no encontrada con ID: " + id));

            // Dar de baja el artículo manufacturado
            promocion.setFechaBaja(LocalDateTime.now());
            if (promocion.getImagenes() != null || !promocion.getImagenes().isEmpty()){
                promocion.getImagenes().forEach(imagen -> {
                    imagen.setFechaBaja(LocalDateTime.now());
                });
            }
            promocionRepository.save(promocion);

            // Dar de baja las imágenes asociadas
            if (promocion.getImagenes() != null && !promocion.getImagenes().isEmpty()) {
                for (Imagen imagen : promocion.getImagenes()) {
                    imagenService.deletePromocion(imagen.getId());
                }
            }

            detalleService.eliminarDetallesLogico(promocion.getPromocionesDetalle());
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void darDeAlta(Long id) {
        Promocion articulo = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion no encontrada"));
        articulo.setFechaBaja(null);
        promocionRepository.save(articulo);
    }
}
