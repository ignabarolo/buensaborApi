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


    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> mostrarCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarCategoriaPorIdConDetalle(id));
    }

    @GetMapping("/insumos/{sucursalId}")
    public ResponseEntity<?> listarCategoriasInsumos(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriasInsumosConDetalle(sucursalId));
    }

    @GetMapping("/insumos/general/{sucursalId}")
    public ResponseEntity<?> listarCategoriasGeneralInsumo(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriasInsumosSinDetalle(sucursalId));
    }

    @GetMapping("/menu/{sucursalId}")
    public ResponseEntity<?> listarCategoriasMenu(@PathVariable Long sucursalId) {
        try {
            return ResponseEntity.ok(categoriaService.obtenerCategoriasHijasMenuConDetalle(sucursalId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/productos/{sucursalId}")
    public ResponseEntity<?> listarCategoriaProducto(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriasProductosSinDetalle(sucursalId));
    }

    @PostMapping
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

            CategoriaArticulo nuevaCategoria = categoriaService.guardarCategoria(categoria);
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