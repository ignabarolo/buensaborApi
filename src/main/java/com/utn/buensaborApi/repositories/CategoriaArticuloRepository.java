package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.CategoriaArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaArticuloRepository extends JpaRepository<CategoriaArticulo, Long> {

    // Categoria por ID
    @Query("SELECT DISTINCT c FROM CategoriaArticulo c " +
            "LEFT JOIN FETCH c.articulo a " +
            "WHERE c.id = :id AND c.fechaBaja IS NULL")
    Optional<CategoriaArticulo> findByIdWithDetails(@Param("id") Long id);

    // Listado selects: solo categorías menú activas (para dropdowns)
    @Query("SELECT DISTINCT c FROM CategoriaArticulo c " +
            "LEFT JOIN FETCH c.articulo a " +
            "WHERE c.categoriaPadre.denominacion = 'Menu' " +
            "AND c.fechaBaja IS NULL " +
            "AND c.sucursal.id = :sucursalId")
    List<CategoriaArticulo> findActiveMenuChildCategories(@Param("sucursalId") Long sucursalId);

    // Listado selects: solo categorías insumo activas (para dropdowns)
    @Query("SELECT DISTINCT c FROM CategoriaArticulo c " +
            "LEFT JOIN FETCH c.articulo a " +
            "WHERE c.categoriaPadre IS NULL " +
            "AND c.denominacion != 'Menu' " +
            "AND c.fechaBaja IS NULL " +
            "AND c.sucursal.id = :sucursalId")
    List<CategoriaArticulo> findActiveInsumosCategoriesWithDetails(@Param("sucursalId") Long sucursalId);

    // Listado ABM: categorías menú con bajas incluidas (para grilla ABM)
    @Query("SELECT DISTINCT c FROM CategoriaArticulo c " +
            "LEFT JOIN FETCH c.articulo a " +
            "LEFT JOIN FETCH a.categoria cat " +
            "LEFT JOIN FETCH a.sucursal s " +
            "LEFT JOIN TREAT(a AS ArticuloManufacturado) am " +
            "LEFT JOIN FETCH am.detalles d " +
            "LEFT JOIN FETCH d.articuloInsumo ai " +
            "WHERE c.categoriaPadre.denominacion = 'Menu' " +
            "AND c.sucursal.id = :sucursalId")
    List<CategoriaArticulo> findMenuChildCategoriesWithDetails(@Param("sucursalId") Long sucursalId);

    // Listado ABM: categorías insumo con bajas incluidas (para grilla ABM)
    @Query("SELECT DISTINCT c FROM CategoriaArticulo c " +
            "LEFT JOIN FETCH c.articulo a " +
            "WHERE c.categoriaPadre IS NULL " +
            "AND c.denominacion != 'Menu' " +
            "AND c.sucursal.id = :sucursalId")
    List<CategoriaArticulo> findAllInsumosCategoriesWithDetails(@Param("sucursalId") Long sucursalId);

    // Verifica existencia por denominación y sucursal, con categoría activa
    boolean existsByDenominacionAndSucursalIdAndFechaBajaIsNull(String denominacion, Long sucursalId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CategoriaArticulo c " +
            "WHERE c.denominacion = :denominacion " +
            "AND c.sucursal.id = :sucursalId " +
            "AND c.fechaBaja IS NULL " +
            "AND c.id <> :id")
    boolean existsByDenominacionAndSucursalIdAndFechaBajaIsNullAndIdNot(
            @Param("denominacion") String denominacion,
            @Param("sucursalId") Long sucursalId,
            @Param("id") Long id);

    @Query("SELECT c FROM CategoriaArticulo c WHERE c.denominacion = 'Menu' AND c.sucursal.id = :sucursalId AND c.fechaBaja IS NULL")
    Optional<CategoriaArticulo> findMenuPadreBySucursal(@Param("sucursalId") Long sucursalId);
}
