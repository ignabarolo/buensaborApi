
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Provincia;
import com.utn.buensaborApi.repository.provinciaRepository;
import java.time.LocalDateTime;
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
         provincia.setFechaAlta(LocalDateTime.now());
         return provinciaRepository.save(provincia);
     }
     
     public Provincia obtenerPorId (Long id) {
         return provinciaRepository.findById(id).orElse(null);
     }
     
    public void eliminar(long id){
    Provincia provincia = provinciaRepository.findById(id).orElse(null);
    if (provincia != null) {
        provincia.setFechaBaja(LocalDateTime.now());
        provinciaRepository.save(provincia);
    }
}

     public Provincia actualizar(Long id, Provincia provinciaActualizada) {
    Provincia provinciaExistente = provinciaRepository.findById(id).orElse(null);
    if (provinciaExistente != null) {
        provinciaExistente.setFechaModificacion(LocalDateTime.now());
        provinciaExistente.setNombre(provinciaActualizada.getNombre());
        provinciaExistente.setPais(provinciaActualizada.getPais());
        
        return provinciaRepository.save(provinciaExistente);
    }
    return null;
}
}
