package com.utn.buensaborApi.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.models.Dtos.Manufacturado.ArticuloManufacturadoDto;
import com.utn.buensaborApi.services.ArticuloManufacturadoService;
import com.utn.buensaborApi.repositories.CategoriaArticuloRepository;
import com.utn.buensaborApi.services.Mappers.ArticuloManufacturadoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/articuloManufacturado")
@RequiredArgsConstructor
public class ArticuloManufacturadoController {

    private final ArticuloManufacturadoService articuloManufacturadoService;
    private final CategoriaArticuloRepository categoriaArticuloRepository;
    private final ArticuloManufacturadoMapper articuloManufacturadoMapper;

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<ArticuloManufacturadoDto> articulos = articuloManufacturadoService.obtenerTodosConDetalles();
            return ResponseEntity.ok(articulos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodosIncluyendoDadosDeBaja() {
        try {
            List<ArticuloManufacturadoDto> articulos = articuloManufacturadoService.obtenerTodosSinFiltrar();
            return ResponseEntity.ok(articulos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarArticuloManufacturadoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloManufacturadoService.buscarPorIdSinDetalle(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/{id}/detallado")
    public ResponseEntity<?> mostrarArticuloManufacturadoPorIdDetallado(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloManufacturadoService.buscarPorIdConDetalle(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearArticuloManufacturado(
            @RequestPart("articuloManufacturado") String articuloManufacturadoJson,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {
        try {
            if (articuloManufacturadoJson == null || articuloManufacturadoJson.isEmpty()) {
                return ResponseEntity.badRequest().body("El JSON del artículo no puede estar vacío.");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            ArticuloManufacturadoDto dto = objectMapper.readValue(articuloManufacturadoJson, ArticuloManufacturadoDto.class);

            // Obtener categoría (ya está en el DTO como objeto con ID)
            Long categoriaId = dto.getCategoria().getId();
            CategoriaArticulo categoria = categoriaArticuloRepository.findById(categoriaId)
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + categoriaId));

            // Mapper de DTO a entidad
            ArticuloManufacturado articulo = articuloManufacturadoMapper.toEntity(dto);
            articulo.setCategoria(categoria); // por seguridad

            Object nuevoArticulo = articuloManufacturadoService.crear(articulo, categoria, imagenes);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoArticulo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el JSON o las imágenes: " + e.getMessage());
        }
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestPart("articuloManufacturado") String articuloManufacturadoJson,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {
        try {
            if (articuloManufacturadoJson == null || articuloManufacturadoJson.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El JSON del artículo no puede estar vacío.");
            }

            // Convertir el JSON del artículo a un objeto ArticuloManufacturado
            ObjectMapper objectMapper = new ObjectMapper();
            ArticuloManufacturado articuloManufacturado = objectMapper.readValue(articuloManufacturadoJson, ArticuloManufacturado.class);

            //Convertir json a un objeto categoriaArticulo
            CategoriaArticulo categoria = new CategoriaArticulo();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(articuloManufacturadoJson);
            if (jsonNode.has("categoria") && jsonNode.get("categoria").has("id")) {
                Long categoriaId = jsonNode.get("categoria").get("id").asLong();
                categoria = categoriaArticuloRepository.findById(categoriaId)
                        .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + categoriaId));
                articuloManufacturado.setCategoria(categoria);
            } else {
                throw new IllegalArgumentException("Se requiere una categoría válida para el artículo manufacturado");
            }

            Object nuevoArticulo = articuloManufacturadoService.actualizar(id,articuloManufacturado, categoria, imagenes);
            return ResponseEntity.status(HttpStatus.OK).body(nuevoArticulo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArticuloManufacturado(@PathVariable Long id) {
        try {
            articuloManufacturadoService.eliminarLogico(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/alta/{id}")
    public ResponseEntity<Void> darDeAlta(@PathVariable Long id) {
        articuloManufacturadoService.darDeAlta(id);
        return ResponseEntity.ok().build();
    }
}
