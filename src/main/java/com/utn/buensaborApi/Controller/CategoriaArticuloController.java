package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.services.CategoriaArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoriaArticuloController {

    @Autowired
    private CategoriaArticuloService categoriaService;

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
        return ResponseEntity.ok(categoriaService.obtenerCategoriaMenuConDetalle(sucursalId));
    }

    @GetMapping("/productos/{sucursalId}")
    public ResponseEntity<?> listarCategoriaProducto(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriasProductosSinDetalle(sucursalId));
    }

    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody CategoriaArticulo categoria) {
        return ResponseEntity.ok(categoriaService.guardarCategoria(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarCategoria(@PathVariable Long id, @RequestBody CategoriaArticulo categoria) {
        categoria.setId(id);
        return ResponseEntity.ok(categoriaService.actualizarCategoria(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarLogico(id);
        return ResponseEntity.ok().build();
    }
}