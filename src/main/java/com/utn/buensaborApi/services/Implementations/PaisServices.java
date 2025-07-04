
package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.Pais;
import com.utn.buensaborApi.repositories.PaisRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaisServices {
     @Autowired
     private PaisRepository paisRepository;
     
     public List<Pais> listarTodos(){
         return paisRepository.findAll();
     }
     
     
     public Pais guardar (Pais pais){
         pais.setFechaAlta(LocalDateTime.now());
         return paisRepository.save(pais);
     }
     
     public Pais obtenerPorId (Long id) {
         return paisRepository.findById(id).orElse(null);
     }
     
     public void eliminar(long id){
        Pais pais = paisRepository.findById(id).orElse(null);
        if (pais != null) {
            pais.setFechaBaja(LocalDateTime.now());
            paisRepository.save(pais);
        }
     }

    public Pais actualizar(Long id, Pais paisDetalles) {
        Optional<Pais> paisOptional = paisRepository.findById(id);
        if (paisOptional.isPresent()) {
            Pais paisExistente = paisOptional.get();
            paisExistente.setFechaModificacion(LocalDateTime.now());
            paisExistente.setNombre(paisDetalles.getNombre()); 
            
            return paisRepository.save(paisExistente);
        } else {
            return null;
        }
    }
     
}
