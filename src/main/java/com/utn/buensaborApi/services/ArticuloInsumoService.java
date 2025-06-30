package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.*;
import com.utn.buensaborApi.models.Dtos.Insumo.ArticuloInsumoDto;
import com.utn.buensaborApi.repositories.*;
import com.utn.buensaborApi.services.Mappers.ArticuloInsumoMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticuloInsumoService {

    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final SucursalInsumoRepository sucursalInsumoRepository;
    private final ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;
    private final PromocionRepository promocionRepository;
    private final  PromocionDetalleRepository promocionDetalleRepository;

    @Autowired
    private final ArticuloInsumoMapper mapper;

    @Autowired
    private SucursalEmpresaRepository sucursalEmpresaRepository;

    public List<ArticuloInsumoDto> listarTodosConDetalle() {
        return articuloInsumoRepository.findAllWithDetails().stream()
            .map(mapper::toDto)
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
        return articuloInsumoRepository.findByIdWithDetails(id).map(mapper::toDto)
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
    public ArticuloInsumo crearArticuloInsumoConSucursalInsumo(ArticuloInsumo articuloInsumo) {
        articuloInsumo.setFechaAlta(LocalDateTime.now());

        SucursalEmpresa sucursal = sucursalEmpresaRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Sucursal 1 no encontrada"));

        articuloInsumo.setSucursal(sucursal);

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
        return savedArticulo;
    }


    @Transactional
    public ArticuloInsumo actualizarArticuloInsumoConSucursalInsumo(ArticuloInsumo articuloInsumo) {
        ArticuloInsumo articuloExistente = articuloInsumoRepository.findByIdAndFechaBajaIsNull(articuloInsumo.getId());

        if (articuloExistente == null) {
            throw new RuntimeException("Artículo Insumo no encontrado con ID: " + articuloInsumo.getId());
        }
        // Guardar el precio anterior para comparación
        Double precioAnterior = articuloExistente.getPrecioCompra();

        articuloInsumo.setFechaModificacion(LocalDateTime.now());
        
        // Actualizar SucursalInsumo asociados
        if (articuloInsumo.getStockPorSucursal() != null) {
            articuloInsumo.getStockPorSucursal().forEach(stock -> {
                if (stock.getId() != null) {
                    stock.setFechaModificacion(LocalDateTime.now());
                } else {
                    stock.setFechaAlta(LocalDateTime.now());
                }
                stock.setArticuloInsumo(articuloInsumo);
                stock.setSucursal(sucursalEmpresaRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: 1")));                sucursalInsumoRepository.save(stock);
                sucursalInsumoRepository.save(stock);

            });
        }
        // Recalcular precio de venta si es necesario
        articuloInsumo.precioCalculado();

        // Guardar el artículo actualizado
        ArticuloInsumo articuloActualizado = articuloInsumoRepository.save(articuloInsumo);

        // Si el precio de compra cambió, actualizar todos los artículos manufacturados que usan este insumo
        if (precioAnterior != null && articuloInsumo.getPrecioCompra() != null &&
                !precioAnterior.equals(articuloInsumo.getPrecioCompra())) {
            actualizarPreciosArticulosManufacturados(articuloActualizado);
        }

        return articuloActualizado;
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

                // La promoción ya tiene método para calcular su precio, así que solo necesitamos guardarla
                promocionRepository.save(promocion);
            }
        }
    }
}