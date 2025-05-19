package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.models.Imagen;
import com.utn.buensaborApi.repository.CategoriaArticuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaArticuloService {

    private final CategoriaArticuloRepository categoriaRepository;
    private final ImagenService imagenService;

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
    public CategoriaArticulo guardarCategoria(CategoriaArticulo categoria, MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            System.out.println("Imagen recibida: " + imagen.getOriginalFilename());
            Imagen nuevaImagen = imagenService.uploadImage(imagen);
            categoria.setImagen(nuevaImagen);
        }else{
            System.out.println("No se recibió imagen o está vacía.");
        }

        categoria.setFechaAlta(LocalDateTime.now());
        return categoriaRepository.save(categoria);
    }

    // Actualizar categoría
    public CategoriaArticulo actualizarCategoria(CategoriaArticulo categoria, MultipartFile imagen) throws IOException {
        if (!categoriaRepository.existsById(categoria.getId())) {
            throw new RuntimeException("Categoría no encontrada con ID: " + categoria.getId());
        }

        // Si hay una nueva imagen, actualizar
        if (imagen != null && !imagen.isEmpty()) {
            // Si ya existe una imagen, actualizarla
            if (categoria.getImagen() != null) {
                Imagen imagenActualizada = imagenService.updateImage(categoria.getImagen().getId(), imagen);
                categoria.setImagen(imagenActualizada);
            } else {
                // Si no existe imagen previa, crear una nueva
                Imagen nuevaImagen = imagenService.uploadImage(imagen);
                categoria.setImagen(nuevaImagen);
            }
        }

        categoria.setFechaModificacion(LocalDateTime.now());
        return categoriaRepository.save(categoria);
    }


    // Eliminado lógico
    public void eliminarLogico(Long id) {
        CategoriaArticulo categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // Eliminar lógicamente la imagen si existe
        if (categoria.getImagen() != null) {
            imagenService.delete(categoria.getImagen().getId());
        }

        categoria.setFechaBaja(LocalDateTime.now());
        categoriaRepository.save(categoria);
    }
}