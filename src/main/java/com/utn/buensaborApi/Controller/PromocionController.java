package com.utn.buensaborApi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.utn.buensaborApi.models.Dtos.Promocion.PromocionDto;
import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.services.Implementations.PromocionServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/promociones")
@Tag(name = "Promociones", description = "Operaciones relacionadas con los promociones")
public class PromocionController extends BaseControllerImpl<Promocion, PromocionServiceImpl>{
    @Autowired
    private PromocionServiceImpl servicio;

    @PostMapping(path = "Create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveDto(
            @RequestPart("promocion") String promocionJson,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            PromocionDto dto = objectMapper.readValue(promocionJson, PromocionDto.class);

            return ResponseEntity.status(HttpStatus.OK).body(servicio.saveDto(dto, imagenes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping(path = "/Update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateDto(
            @PathVariable Long id,
            @RequestPart("promocion") String promocionJson,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            PromocionDto dto = objectMapper.readValue(promocionJson, PromocionDto.class);

            var res = servicio.updateDto(id, dto, imagenes);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    @GetMapping("")
    public ResponseEntity<?> getAll(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.findAllDto());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Por favor intente de nuevo.\"}");
        }
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.findByIdDto(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":" + "\"" + e.getMessage() + ".\"}");
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            servicio.eliminarLogico(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/alta/{id}")
    public ResponseEntity<Void> darDeAlta(@PathVariable Long id) {
        servicio.darDeAlta(id);
        return ResponseEntity.ok().build();
    }

}
