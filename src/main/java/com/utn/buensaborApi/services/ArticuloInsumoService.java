package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.repositories.ArticuloInsumoRepository;
import com.utn.buensaborApi.repositories.SucursalInsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloInsumoService {

    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final SucursalInsumoRepository sucursalInsumoRepository;

    public List<ArticuloInsumo> listarTodosConDetalle() {
    return articuloInsumoRepository.findAllWithDetails();
}

    
    public ArticuloInsumo buscarPorIdConDetalle(Long id) {
        return articuloInsumoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Artículo Insumo no encontrado con ID: " + id));
    }

    //Listar Insumos por sucursal con detalle
    public List<ArticuloInsumo> listarPorSucursalConDetalle(Long sucursalId) {
        return articuloInsumoRepository.findAllBySucursalWithDetails(sucursalId);
    }

    //Listar Insumos para elaborar por sucursal
    public List<ArticuloInsumo> listarParaElaborarPorSucursal(Long sucursalId) {
        return articuloInsumoRepository.findAllForElaborationBySucursal(sucursalId);
    }

    //Guardar Insumo
    @Transactional
    public ArticuloInsumo crearArticuloInsumoConSucursalInsumo(ArticuloInsumo articuloInsumo) {
        articuloInsumo.setFechaAlta(LocalDateTime.now());
        
        // Guardar primero el ArticuloInsumo
        ArticuloInsumo savedArticulo = articuloInsumoRepository.save(articuloInsumo);

        // Crear y guardar SucursalInsumo asociado
        if (articuloInsumo.getStockPorSucursal() != null) {
            articuloInsumo.getStockPorSucursal().forEach(stock -> {
                stock.setArticuloInsumo(articuloInsumo);
                stock.setFechaAlta(LocalDateTime.now());
                sucursalInsumoRepository.save(stock);
            });
        }

        return savedArticulo;
    }

    @Transactional
    public ArticuloInsumo actualizarArticuloInsumoConSucursalInsumo(ArticuloInsumo articuloInsumo) {
        if (!articuloInsumoRepository.existsById(articuloInsumo.getId())) {
            throw new RuntimeException("Artículo Insumo no encontrado con ID: " + articuloInsumo.getId());
        }

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
                sucursalInsumoRepository.save(stock);
            });
        }
        return articuloInsumoRepository.save(articuloInsumo);
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

}