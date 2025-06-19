package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Dtos.Promocion.PromocionDto;
import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.services.Implementations.PromocionServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/promociones")
@Tag(name = "Promociones", description = "Operaciones relacionadas con los promociones")
public class PromocionController extends BaseControllerImpl<Promocion, PromocionServiceImpl>{
    @Autowired
    private PromocionServiceImpl servicio;

    @PostMapping("/Create")
    public ResponseEntity<?> saveDto(@RequestBody PromocionDto dto){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(servicio.saveDto(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<?> updateDto(@RequestBody PromocionDto dto, @PathVariable Long id){
        try{
            var res = servicio.updateDto(id, dto);
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

}
