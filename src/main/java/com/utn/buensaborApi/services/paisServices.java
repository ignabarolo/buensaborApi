
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Pais;
import com.utn.buensaborApi.repository.paisRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Enzo
 */
@Service
public class paisServices {
          @Autowired
     private paisRepository paisRepository;
     
     public List<Pais> listarTodos(){
         return paisRepository.findAll();
     }
     
     public Pais guardar (Pais pais){
         return paisRepository.save(pais);
     }
     
     public Pais obtenerPorId (Long id) {
         return paisRepository.findById(id).orElse(null);
     }
     public void eliminar (long id){
         paisRepository.deleteById(id);
     }   
}
