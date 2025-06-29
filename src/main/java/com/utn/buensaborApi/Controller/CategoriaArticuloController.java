package com.utn.buensaborApi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.models.Dtos.Manufacturado.CategoriaArticuloDto;
import com.utn.buensaborApi.models.SucursalEmpresa;
import com.utn.buensaborApi.services.CategoriaArticuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaArticuloController {

    private final CategoriaArticuloService categoriaService;

    // Categoria por ID
    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> mostrarCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarCategoriaPorIdConDetalle(id));
    }

    // Listado ABM: categorías menú con bajas incluidas (para grilla ABM)
    @GetMapping("/menu/abm/{sucursalId}")
    public ResponseEntity<?> listarCategoriasMenuConBajas(@PathVariable Long sucursalId) {
        try {
            var res = categoriaService.obtenerCategoriasMenuConBajas(sucursalId);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Listado selects: solo categorías menú activas (para dropdowns)
    @GetMapping("/menu/activas/{sucursalId}")
    public ResponseEntity<?> listarCategoriasMenuActivas(@PathVariable Long sucursalId) {
        try {
            return ResponseEntity.ok(categoriaService.obtenerCategoriasMenuActivas(sucursalId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Listado ABM: categorías insumo con bajas incluidas (para grilla ABM)
    @GetMapping("/insumos/abm/{sucursalId}")
    public ResponseEntity<?> listarCategoriasInsumosConBajas(@PathVariable Long sucursalId) {
        try {
            return ResponseEntity.ok(categoriaService.obtenerCategoriasInsumosConBajas(sucursalId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Listado selects: solo categorías insumo activas (para dropdowns)
    @GetMapping("/insumos/activas/{sucursalId}")
    public ResponseEntity<?> listarCategoriasInsumosActivas(@PathVariable Long sucursalId) {
        try {
            return ResponseEntity.ok(categoriaService.obtenerCategoriasInsumosActivas(sucursalId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> crearCategoria(@RequestBody CategoriaArticuloDto dto) {
        try {
            // Validar datos mínimos
            if (dto.getSucursalId() == null) {
                System.out.println("Sucursal es obligatoria, dto.getSucursalId() es null");
                return ResponseEntity.badRequest().body("Sucursal es obligatoria");
            }

            // Mapear DTO a entidad parcial para pasar al service
            CategoriaArticulo categoria = new CategoriaArticulo();
            categoria.setDenominacion(dto.getDenominacion());
            categoria.setFechaBaja(dto.getFechaBaja());

            // Crear y asignar SucursalEmpresa sólo con id
            SucursalEmpresa sucursal = new SucursalEmpresa();
            sucursal.setId(dto.getSucursalId());
            categoria.setSucursal(sucursal);

            // Si viene categoriaPadreId, asignar también
            if (dto.getCategoriaPadreId() != null) {
                CategoriaArticulo padre = new CategoriaArticulo();
                padre.setId(dto.getCategoriaPadreId());
                categoria.setCategoriaPadre(padre);
            } else {
                System.out.println("Categoria padre no enviada (null)");
            }

            // Inicializar lista vacía de articulos
            categoria.setArticulo(new ArrayList<>());

            // Validar duplicado
            boolean existe = categoriaService.existeCategoriaActiva(
                    categoria.getDenominacion(),
                    categoria.getSucursal().getId()
            );

            if (existe) {
                System.out.println("Categoria duplicada detectada, retornando 400");
                return ResponseEntity.badRequest()
                        .body("Ya existe una categoría activa con esa denominación en esta sucursal.");
            }

            CategoriaArticulo nuevaCategoria = categoriaService.guardarCategoria(categoria, dto.getCategoriaInsumo());
            System.out.println("Categoria guardada con id: " + nuevaCategoria.getId());

            System.out.println("=== crearCategoria - Fin ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);

        } catch (RuntimeException e) {
            System.out.println("Error en crearCategoria:");
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarCategoria(@PathVariable Long id, @RequestBody CategoriaArticuloDto dto) {
        try {
            if (dto.getSucursalId() == null) {
                return ResponseEntity.badRequest().body("Sucursal es obligatoria");
            }

            CategoriaArticulo categoria = new CategoriaArticulo();
            categoria.setId(id);
            categoria.setDenominacion(dto.getDenominacion());

            SucursalEmpresa sucursal = new SucursalEmpresa();
            sucursal.setId(dto.getSucursalId());
            categoria.setSucursal(sucursal);

            if (dto.getCategoriaPadreId() != null) {
                CategoriaArticulo padre = new CategoriaArticulo();
                padre.setId(dto.getCategoriaPadreId());
                categoria.setCategoriaPadre(padre);
            }else {
                categoria.setCategoriaPadre(null);
            }

            categoria.setArticulo(new ArrayList<>());

            CategoriaArticulo actualizada = categoriaService.actualizarCategoria(categoria);
            return ResponseEntity.ok(actualizada);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            categoriaService.eliminarLogico(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al eliminar la categoría: " + e.getMessage());
        }
    }
}