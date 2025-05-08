
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.repository.localidadRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Enzo
 */
@Service
public class localidadServices {
    
    @Autowired
     private localidadRepository localidadRepository;
     
     public List<Localidad> listarTodos(){
         return localidadRepository.findAll();
     }
     
     public Localidad guardar (Localidad localidad){
         return localidadRepository.save(localidad);
     }
     
     public Localidad obtenerPorId (Long id) {
         return localidadRepository.findById(id).orElse(null);
     }
     public void eliminar (long id){
         localidadRepository.deleteById(id);
     }   
}
