
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Provincia;
import com.utn.buensaborApi.repository.provinciaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class provinciaServices {
    @Autowired
     private provinciaRepository provinciaRepository;
     
     public List<Provincia> listarTodos(){
         return provinciaRepository.findAll();
     }
     
     public Provincia guardar (Provincia provincia){
         return provinciaRepository.save(provincia);
     }
     
     public Provincia obtenerPorId (Long id) {
         return provinciaRepository.findById(id).orElse(null);
     }
     public void eliminar (long id){
         provinciaRepository.deleteById(id);
     }
     public Provincia actualizar(Long id, Provincia provinciaActualizada) {
    Provincia provinciaExistente = provinciaRepository.findById(id).orElse(null);
    if (provinciaExistente != null) {
        provinciaExistente.setNombre(provinciaActualizada.getNombre());
        provinciaExistente.setPais(provinciaActualizada.getPais());
        return provinciaRepository.save(provinciaExistente);
    }
    return null;
}
}
