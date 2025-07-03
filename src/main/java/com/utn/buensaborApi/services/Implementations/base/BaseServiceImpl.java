package com.utn.buensaborApi.services.Implementations.base;

import com.utn.buensaborApi.models.base.BaseEntity;
import com.utn.buensaborApi.repositories.base.BaseRepository;
import com.utn.buensaborApi.services.Interfaces.base.BaseService;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<E extends BaseEntity, ID extends Serializable> implements BaseService<E, ID> {
    protected BaseRepository<E, ID> baseRepository;

    public BaseServiceImpl(BaseRepository<E, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    public List<E> findAll() throws Exception{
        try {
            List<E> entities = baseRepository.findAll();
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public E findById(ID id) throws Exception{
        try {
            Optional<E> entityOptional = baseRepository.findById(id);
            return entityOptional.get();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public E save(E entity) throws Exception{
        try {
            entity.setFechaAlta(LocalDateTime.now());
            entity.setFechaModificacion(LocalDateTime.now());
            entity.setFechaBaja(LocalDateTime.of(2099, 01, 01, 00,00,00));
            entity = baseRepository.save(entity);

            return entity;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public E update(ID id, E entity) throws Exception{
        try {
            Optional<E> entityOptional = baseRepository.findById(id);
            E entityUpdate = entityOptional.get();
            entity.setFechaModificacion(LocalDateTime.now());
            entityUpdate = baseRepository.save(entity);
            return entityUpdate;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean delete(ID id) throws Exception{
        try {
            if (baseRepository.existsById(id)){
                Optional<E> entityOptional = baseRepository.findById(id);
                E entityDelete = entityOptional.get();

                entityDelete.setFechaBaja(LocalDateTime.now());
                entityDelete.setFechaModificacion(LocalDateTime.now());

                baseRepository.save(entityDelete);
                return true;
            } else {
                throw new Exception();
            }


        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
