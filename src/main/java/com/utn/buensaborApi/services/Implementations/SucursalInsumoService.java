package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.dtos.Manufacturado.SucursalInsumoDto;
import com.utn.buensaborApi.models.SucursalInsumo;
import com.utn.buensaborApi.repositories.SucursalInsumoRepository;
import com.utn.buensaborApi.mappers.SucursalInsumoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SucursalInsumoService {

    private final SucursalInsumoRepository sucursalInsumoRepository;

    @Autowired
    private final SucursalInsumoMapper sucursalInsumoMapper;

    // Listar por sucursal (activos)
    @Transactional(readOnly = true)
    public List<SucursalInsumoDto> findBySucursal(Long idSucursal) {
        List<SucursalInsumo> sucursalInsumos = sucursalInsumoRepository.findBySucursalIdAndFechaBajaIsNullWithArticuloInsumo(idSucursal);
        List<SucursalInsumoDto> sucursalInsumosDto = sucursalInsumoMapper.toDtoList(sucursalInsumos);
        return sucursalInsumosDto;
    }

    // Buscar por ID (activo)
    @Transactional(readOnly = true)
    public SucursalInsumoDto findById(Long id) {
        Optional<SucursalInsumo> sucursalInsumoOptional = sucursalInsumoRepository.findByIdAndFechaBajaIsNull(id);
        SucursalInsumo sucursalInsumo = sucursalInsumoOptional.get();
        SucursalInsumoDto sucursalInsumoDto = sucursalInsumoMapper.toDto(sucursalInsumo);
        return sucursalInsumoDto;
    }

    // Buscar por ArticuloInsumo (activos)
    @Transactional(readOnly = true)
    public List<SucursalInsumo> findByArticuloInsumo(Long idArticuloInsumo) {
        return sucursalInsumoRepository.findByArticuloInsumoIdAndFechaBajaIsNull(idArticuloInsumo);
    }

    // Crear nuevo
    public SucursalInsumo save(SucursalInsumo sucursalInsumo) {
        if (sucursalInsumo.getStockActual() == null || sucursalInsumo.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser negativo o nulo");
        }
        if (sucursalInsumo.getStockMinimo() == null || sucursalInsumo.getStockMinimo() < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo o nulo");
        }
        return sucursalInsumoRepository.save(sucursalInsumo);
    }

    // Modificar
    public SucursalInsumo update(Long id, SucursalInsumo sucursalInsumo) {
        Optional<SucursalInsumo> sucursalInsumoOptional = sucursalInsumoRepository.findByIdAndFechaBajaIsNull(id);

        if (sucursalInsumoOptional.isEmpty()) {
            throw new RuntimeException("SucursalInsumo no encontrada");
        }

        SucursalInsumo existingSucursalInsumo = sucursalInsumoOptional.get();

        if (sucursalInsumo.getStockActual() != null) {
            if (sucursalInsumo.getStockActual() < 0) {
                throw new IllegalArgumentException("El stock actual no puede ser negativo");
            }
            existingSucursalInsumo.setStockActual(sucursalInsumo.getStockActual());
        }

        if (sucursalInsumo.getStockMinimo() != null) {
            if (sucursalInsumo.getStockMinimo() < 0) {
                throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
            }
            existingSucursalInsumo.setStockMinimo(sucursalInsumo.getStockMinimo());
        }

        return sucursalInsumoRepository.save(existingSucursalInsumo);
    }

    // Eliminar (lógico)
    public boolean delete(Long id) {
        Optional<SucursalInsumo> sucursalInsumoOptional = sucursalInsumoRepository.findByIdAndFechaBajaIsNull(id);

        if (sucursalInsumoOptional.isEmpty()) {
            throw new RuntimeException("SucursalInsumo no encontrada");
        }

        SucursalInsumo sucursalInsumo = sucursalInsumoOptional.get();
        sucursalInsumo.setFechaBaja(LocalDateTime.now());
        sucursalInsumoRepository.save(sucursalInsumo);
        return true;
    }
    @Transactional
    public SucursalInsumo actualizarStockActual(Long articuloInsumoId, Long sucursalId, Double nuevoStockActual) {
        if (nuevoStockActual < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser negativo");
        }

        SucursalInsumo sucursalInsumo = sucursalInsumoRepository.findBySucursalIdAndArticuloInsumoIdAndFechaBajaIsNull(sucursalId, articuloInsumoId)
                .orElseThrow(() -> new RuntimeException("No se encontró registro de stock para el artículo " + articuloInsumoId + " en la sucursal " + sucursalId));

        sucursalInsumo.setStockActual(nuevoStockActual);
        sucursalInsumo.setFechaModificacion(LocalDateTime.now());

        return sucursalInsumoRepository.save(sucursalInsumo);
    }

}