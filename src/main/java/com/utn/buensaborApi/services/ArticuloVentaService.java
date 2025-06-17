package com.utn.buensaborApi.services;
import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.models.Dtos.ProductoVenta.ArticuloVentaDto;
import com.utn.buensaborApi.repositories.ArticuloInsumoRepository;
import com.utn.buensaborApi.repositories.ArticuloManufacturadoRepository;
import com.utn.buensaborApi.repositories.PromocionRepository;
import com.utn.buensaborApi.services.Mappers.CategoriaArticuloMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticuloVentaService {

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private CategoriaArticuloMapper categoriaArticuloMapper;

    public List<ArticuloVentaDto> obtenerTodosLosArticulosParaVenta() {
        List<ArticuloVentaDto> resultado = new ArrayList<>();

        // Obtener insumos para venta (paraElaborar = false)
        List<ArticuloInsumo> insumos = articuloInsumoRepository.findByEsParaElaborarFalseAndFechaBajaIsNull();
        insumos.forEach(insumo -> {
            ArticuloVentaDto dto = new ArticuloVentaDto();
            dto.setId(insumo.getId());
            dto.setTipo("INSUMO");
            dto.setDenominacion(insumo.getDenominacion());
            dto.setCategoriaArticulo(categoriaArticuloMapper.toDto(insumo.getCategoria()));
            dto.setPrecioVenta(BigDecimal.valueOf(insumo.getPrecioVenta()));
            dto.setStockDisponible(insumo.obtenerStockMaximo());
            // Establecer URL de imagen si existe
            if (insumo.getImagenes() != null && !insumo.getImagenes().isEmpty()) {
                dto.setImagenUrl(insumo.getImagenes().iterator().next().getNombre());
            }

            resultado.add(dto);
        });

        // Obtener artículos manufacturados
        List<ArticuloManufacturado> manufacturados = articuloManufacturadoRepository.findAllByFechaBajaIsNull();
        manufacturados.forEach(manufacturado -> {
            ArticuloVentaDto dto = new ArticuloVentaDto();
            dto.setId(manufacturado.getId());
            dto.setTipo("MANUFACTURADO");
            dto.setDenominacion(manufacturado.getDenominacion());
            dto.setDescripcion(manufacturado.getDescripcion());
            dto.setCategoriaArticulo(categoriaArticuloMapper.toDto(manufacturado.getCategoria()));            dto.setPrecioVenta(BigDecimal.valueOf(manufacturado.getPrecioVenta()));
            dto.setStockDisponible(manufacturado.stockMaximoCalculado());

            // Establecer URL de imagen si existe
            if (manufacturado.getImagenes() != null && !manufacturado.getImagenes().isEmpty()) {
                dto.setImagenUrl(manufacturado.getImagenes().iterator().next().getNombre());
            }

            resultado.add(dto);
        });

        // Obtener promociones activas
            List<Promocion> promociones = promocionRepository.findActivePromotions(LocalDate.now());        promociones.forEach(promocion -> {
            ArticuloVentaDto dto = new ArticuloVentaDto();
            dto.setId(promocion.getId());
            dto.setTipo("PROMOCION");
            dto.setDenominacion(promocion.getDenominacion());
            dto.setPrecioVenta(promocion.getPrecioVenta());
            dto.setStockDisponible(promocion.obtenerStockDisponible());

            // Establecer URL de imagen si existe
            if (promocion.getImagenes() != null) {
                dto.setImagenUrl(promocion.getImagenes().iterator().next().getNombre());
            }

            resultado.add(dto);
        });

        return resultado;
    }


}
