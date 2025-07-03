
package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.repositories.LocalidadRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalidadServices {
    
    @Autowired
     private LocalidadRepository localidadRepository;
     
     public List<Localidad> listarTodos(){
         return localidadRepository.findAll();
     }
     
     public Localidad guardar (Localidad localidad){
         localidad.setFechaAlta(LocalDateTime.now());
         return localidadRepository.save(localidad);
     }
     
     public Localidad obtenerPorId (Long id) {
         return localidadRepository.findById(id).orElse(null);
     }
  public void eliminar(long id){
    Localidad localidad = localidadRepository.findById(id).orElse(null);
    if (localidad != null) {
        localidad.setFechaBaja(LocalDateTime.now());
        localidadRepository.save(localidad);
    }
}

     public Localidad actualizar(Long id, Localidad localidadActualizada) {
    Localidad existente = localidadRepository.findById(id).orElse(null);
    if (existente != null) {
        existente.setNombre(localidadActualizada.getNombre());
        existente.setProvincia(localidadActualizada.getProvincia());
        existente.setFechaModificacion(LocalDateTime.now());
        return localidadRepository.save(existente);
    }
    return null;
}

}
