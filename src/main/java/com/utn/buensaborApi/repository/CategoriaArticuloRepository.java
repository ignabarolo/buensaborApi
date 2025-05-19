package com.utn.buensaborApi.repository;

import com.utn.buensaborApi.models.CategoriaArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaArticuloRepository extends JpaRepository<CategoriaArticulo, Long> {

    @Query("SELECT DISTINCT c FROM CategoriaArticulo c " +
            "LEFT JOIN FETCH c.articulo a " +
            "WHERE c.id = :id AND c.fechaBaja IS NULL")
    Optional<CategoriaArticulo> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT c FROM CategoriaArticulo c " +
            "LEFT JOIN FETCH c.articulo a " +
            "WHERE c.categoriaPadre IS NULL " +
            "AND c.denominacion != 'Menu' " +
            "AND c.fechaBaja IS NULL " +
            "AND c.sucursal.id = :sucursalId")
    List<CategoriaArticulo> findInsumosCategoriesWithDetails(@Param("sucursalId") Long sucursalId);

    @Query("SELECT c FROM CategoriaArticulo c " +
            "WHERE c.categoriaPadre IS NULL " +
            "AND c.denominacion != 'Menu' " +
            "AND c.fechaBaja IS NULL " +
            "AND c.sucursal.id = :sucursalId")
    List<CategoriaArticulo> findParentInsumosCategoriesNoDetails(@Param("sucursalId") Long sucursalId);

    @Query("SELECT DISTINCT c FROM CategoriaArticulo c " +
            "LEFT JOIN FETCH c.articulo a " +
            "WHERE c.denominacion = 'Menu' " +
            "AND c.fechaBaja IS NULL " +
            "AND c.sucursal.id = :sucursalId")
    Optional<CategoriaArticulo> findMenuCategoryWithDetails(@Param("sucursalId") Long sucursalId);

    @Query("SELECT c FROM CategoriaArticulo c " +
            "WHERE c.categoriaPadre.denominacion = 'Menu' " +
            "AND c.fechaBaja IS NULL " +
            "AND c.sucursal.id = :sucursalId")
    List<CategoriaArticulo> findProductCategoriesNoDetails(@Param("sucursalId") Long sucursalId);

}
