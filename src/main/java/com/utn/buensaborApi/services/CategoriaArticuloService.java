package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.models.Imagen;
import com.utn.buensaborApi.models.SucursalEmpresa;
import com.utn.buensaborApi.repositories.CategoriaArticuloRepository;
import com.utn.buensaborApi.repositories.SucursalEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaArticuloService {

    private final CategoriaArticuloRepository categoriaRepository;

    private final ImagenService imagenService;

    @Autowired
    private SucursalEmpresaRepository sucursalRepository;

    // Buscar categoría por ID con todo el detalle
    public CategoriaArticulo buscarCategoriaPorIdConDetalle(Long id) {
        return categoriaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    // Listar categorías de insumos y manufacturados con detalle
    public List<CategoriaArticulo> obtenerCategoriasInsumosConDetalle(Long sucursalId) {
        return categoriaRepository.findAllInsumosCategoriesWithDetails(sucursalId);
    }

    // Listar categorías generales de insumos sin detalle
    public List<CategoriaArticulo> obtenerCategoriasInsumosSinDetalle(Long sucursalId) {
        return categoriaRepository.findParentInsumosCategoriesNoDetails(sucursalId);
    }

    // Listar categoría Menú con detalle
    public List<CategoriaArticulo> obtenerCategoriasHijasMenuConDetalle(Long sucursalId) {
        List<CategoriaArticulo> categorias = categoriaRepository.findMenuChildCategoriesWithDetails(sucursalId);
        if (categorias.isEmpty()) {
            throw new RuntimeException("No se encontraron categorías hijas de Menú");
        }
        return categorias;
    }

    // Listar categorías de productos sin detalle
    public List<CategoriaArticulo> obtenerCategoriasProductosSinDetalle(Long sucursalId) {
        return categoriaRepository.findProductCategoriesNoDetails(sucursalId);
    }

    public CategoriaArticulo guardarCategoria(CategoriaArticulo categoria) {
        if (categoria.getSucursal() == null || categoria.getSucursal().getId() == null) {
            throw new RuntimeException("Sucursal es obligatoria");
        }

        // Si se envió un padre válido, se busca y asigna. Si no, se deja como null.
        if (categoria.getCategoriaPadre() != null && categoria.getCategoriaPadre().getId() != null) {
            CategoriaArticulo padre = categoriaRepository.findById(categoria.getCategoriaPadre().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada con ID: " + categoria.getCategoriaPadre().getId()));
            categoria.setCategoriaPadre(padre);
        } else {
            categoria.setCategoriaPadre(null); // Se deja sin padre si no vino nada
        }


        // Validar que la sucursal exista
        SucursalEmpresa sucursal = sucursalRepository.findById(categoria.getSucursal().getId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + categoria.getSucursal().getId()));

        categoria.setSucursal(sucursal);

        if (categoria.getCategoriaPadre() != null && categoria.getCategoriaPadre().getId() != null) {
            CategoriaArticulo padre = categoriaRepository.findById(categoria.getCategoriaPadre().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada con ID: " + categoria.getCategoriaPadre().getId()));
            categoria.setCategoriaPadre(padre);
        } else {
            categoria.setCategoriaPadre(null);
        }

        boolean existe = categoriaRepository.existsByDenominacionAndSucursalIdAndFechaBajaIsNull(
                categoria.getDenominacion(),
                categoria.getSucursal().getId()
        );
        if (existe) {
            throw new RuntimeException("Ya existe una categoría activa con esa denominación.");
        }
        categoria.setFechaAlta(LocalDateTime.now());
        return categoriaRepository.save(categoria);
    }

    public CategoriaArticulo actualizarCategoria(CategoriaArticulo categoria) {
        if (!categoriaRepository.existsById(categoria.getId())) {
            throw new RuntimeException("Categoría no encontrada con ID: " + categoria.getId());
        }

        if (categoria.getSucursal() == null || categoria.getSucursal().getId() == null) {
            throw new RuntimeException("Sucursal es obligatoria");
        }

        SucursalEmpresa sucursal = sucursalRepository.findById(categoria.getSucursal().getId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + categoria.getSucursal().getId()));
        categoria.setSucursal(sucursal);

        if (categoria.getCategoriaPadre() != null && categoria.getCategoriaPadre().getId() != null) {
            CategoriaArticulo padre = categoriaRepository.findById(categoria.getCategoriaPadre().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada con ID: " + categoria.getCategoriaPadre().getId()));
            categoria.setCategoriaPadre(padre);
        } else {
            categoria.setCategoriaPadre(null);
        }

        boolean existeDuplicado = existeCategoriaActivaParaOtroId(
                categoria.getDenominacion(),
                categoria.getSucursal().getId(),
                categoria.getId()
        );
        if (existeDuplicado) {
            throw new RuntimeException("Ya existe una categoría activa con esa denominación en esta sucursal.");
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

    public boolean existeCategoriaActiva(String denominacion, Long sucursalId) {
        return categoriaRepository.existsByDenominacionAndSucursalIdAndFechaBajaIsNull(denominacion, sucursalId);
    }

    public boolean existeCategoriaActivaParaOtroId(String denominacion, Long sucursalId, Long id) {
        return categoriaRepository.existsByDenominacionAndSucursalIdAndFechaBajaIsNullAndIdNot(denominacion, sucursalId, id);
    }
}