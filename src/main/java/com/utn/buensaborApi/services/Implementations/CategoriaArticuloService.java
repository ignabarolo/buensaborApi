package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.models.SucursalEmpresa;
import com.utn.buensaborApi.repositories.CategoriaArticuloRepository;
import com.utn.buensaborApi.repositories.SucursalEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaArticuloService {

    private final CategoriaArticuloRepository categoriaRepository;

    @Autowired
    private SucursalEmpresaRepository sucursalRepository;

    // Categoria por ID
    public CategoriaArticulo buscarCategoriaPorIdConDetalle(Long id) {
        return categoriaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    // Listado selects: solo categorías menú activas (para dropdowns)
    public List<CategoriaArticulo> obtenerCategoriasMenuActivas(Long sucursalId) {
        return categoriaRepository.findActiveMenuChildCategories(sucursalId);
    }

    // Listado selects: solo categorías insumo activas (para dropdowns)
    public List<CategoriaArticulo> obtenerCategoriasInsumosActivas(Long sucursalId) {
        return categoriaRepository.findActiveInsumosCategoriesWithDetails(sucursalId);
    }

    // Listado ABM: categorías menú con bajas incluidas (para grilla ABM)
    public List<CategoriaArticulo> obtenerCategoriasMenuConBajas(Long sucursalId) {
        return categoriaRepository.findMenuChildCategoriesWithDetails(sucursalId);
    }

    // Listado ABM: categorías insumo con bajas incluidas (para grilla ABM)
    public List<CategoriaArticulo> obtenerCategoriasInsumosConBajas(Long sucursalId) {
        return categoriaRepository.findAllInsumosCategoriesWithDetails(sucursalId);
    }

    public CategoriaArticulo guardarCategoria(CategoriaArticulo categoria, boolean esCategoriaInsumo) {
        if (categoria.getSucursal() == null || categoria.getSucursal().getId() == null) {
            throw new RuntimeException("Sucursal es obligatoria");
        }
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

        if (!esCategoriaInsumo){
            var categoriaMenu = categoriaRepository.findMenuPadreBySucursal(1L).get();
            categoria.setCategoriaPadre(categoriaMenu);
        }

        categoria.setFechaAlta(LocalDateTime.now());
        categoria.setFechaModificacion(LocalDateTime.now());
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