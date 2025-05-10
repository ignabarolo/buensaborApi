package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.UnidadMedida;
import com.utn.buensaborApi.repository.UnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UnidadMedidaService {

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    //Listar las unidades de medida activas
    @Transactional(readOnly = true)
    public List<UnidadMedida> findAll() {
        return unidadMedidaRepository.findByFechaBajaIsNull();
    }

    //Buscar una unidad de medida por id (activa)
    @Transactional(readOnly = true)
    public Optional<UnidadMedida> findById(Long id) {
        return unidadMedidaRepository.findByIdAndFechaBajaIsNull(id);
    }

    //Guardar una unidad de medida
    @Transactional
    public UnidadMedida save(UnidadMedida unidadMedida) {
        unidadMedida.setFechaAlta(LocalDateTime.now());
        return unidadMedidaRepository.save(unidadMedida);
    }

    //Editar una unidad de medida
    @Transactional
    public UnidadMedida update(UnidadMedida unidadMedida) {
        Optional<UnidadMedida> unidadMedidaExistente = unidadMedidaRepository.findByIdAndFechaBajaIsNull(unidadMedida.getId());
        
        if (!unidadMedidaExistente.isPresent()) {
            throw new RuntimeException("Unidad de medida no encontrada o dada de baja");
        }

        unidadMedida.setFechaModificacion(LocalDateTime.now());
        unidadMedida.setFechaAlta(unidadMedidaExistente.get().getFechaAlta());
        return unidadMedidaRepository.save(unidadMedida);
    }

    //Eliminar una unidad de medida
    @Transactional
    public void delete(Long id) {
        Optional<UnidadMedida> unidadMedidaOptional = unidadMedidaRepository.findByIdAndFechaBajaIsNull(id);
        
        if (!unidadMedidaOptional.isPresent()) {
            throw new RuntimeException("Unidad de medida no encontrada o ya fue dada de baja");
        }

        UnidadMedida unidadMedida = unidadMedidaOptional.get();
        unidadMedida.setFechaBaja(LocalDateTime.now());
        unidadMedidaRepository.save(unidadMedida);
    }
}