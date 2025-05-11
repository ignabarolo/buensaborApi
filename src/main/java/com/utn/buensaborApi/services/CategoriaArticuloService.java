package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.repository.CategoriaArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaArticuloService {

    @Autowired
    private CategoriaArticuloRepository categoriaRepository;

    // Buscar categoría por ID con todo el detalle
    public CategoriaArticulo buscarCategoriaPorIdConDetalle(Long id) {
        return categoriaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    // Listar categorías de insumos con detalle
    public List<CategoriaArticulo> obtenerCategoriasInsumosConDetalle(Long sucursalId) {
        return categoriaRepository.findInsumosCategoriesWithDetails(sucursalId);
    }

    // Listar categorías generales de insumos sin detalle
    public List<CategoriaArticulo> obtenerCategoriasInsumosSinDetalle(Long sucursalId) {
        return categoriaRepository.findParentInsumosCategoriesNoDetails(sucursalId);
    }

    // Listar categoría Menú con detalle
    public CategoriaArticulo obtenerCategoriaMenuConDetalle(Long sucursalId) {
        return categoriaRepository.findMenuCategoryWithDetails(sucursalId)
                .orElseThrow(() -> new RuntimeException("Categoría Menú no encontrada"));
    }

    // Listar categorías de productos sin detalle
    public List<CategoriaArticulo> obtenerCategoriasProductosSinDetalle(Long sucursalId) {
        return categoriaRepository.findProductCategoriesNoDetails(sucursalId);
    }

    // Guardar nueva categoría
    public CategoriaArticulo guardarCategoria(CategoriaArticulo categoria) {
        categoria.setFechaAlta(LocalDateTime.now());
        return categoriaRepository.save(categoria);
    }

    // Actualizar categoría
    public CategoriaArticulo actualizarCategoria(CategoriaArticulo categoria) {
        if (!categoriaRepository.existsById(categoria.getId())) {
            throw new RuntimeException("Categoría no encontrada con ID: " + categoria.getId());
        }
        categoria.setFechaModificacion(LocalDateTime.now());
        return categoriaRepository.save(categoria);
    }

    // Eliminado lógico
    public void eliminarLogico(Long id) {
        CategoriaArticulo categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        categoria.setFechaBaja(LocalDateTime.now());
        categoriaRepository.save(categoria);
    }
}