package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.SucursalInsumo;
import com.utn.buensaborApi.repository.SucursalInsumoRepository;
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

    // Listar por sucursal (activos)
    @Transactional(readOnly = true)
    public List<SucursalInsumo> findBySucursal(Long idSucursal) {
        return sucursalInsumoRepository.findBySucursalIdAndFechaBajaIsNull(idSucursal);
    }

    // Buscar por ID (activo)
    @Transactional(readOnly = true)
    public Optional<SucursalInsumo> findById(Long id) {
        return sucursalInsumoRepository.findByIdAndFechaBajaIsNull(id);
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

}
