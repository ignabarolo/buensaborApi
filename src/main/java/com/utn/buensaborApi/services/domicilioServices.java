
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.repository.domicilioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Enzo
 */

@Service
public class domicilioServices {
     @Autowired
     private domicilioRepository domicilioRepository;
     
     public List<Domicilio> listarTodos(){
         return domicilioRepository.findAll();
     }
     
     public Domicilio guardar (Domicilio domicilio){
         return domicilioRepository.save(domicilio);
     }
     
     public Domicilio obtenerPorId (Long id) {
         return domicilioRepository.findById(id).orElse(null);
     }
     public void eliminar (long id){
         domicilioRepository.deleteById(id);
     }

    public Domicilio actualizarDomicilio(Long id, Domicilio nuevoDomicilio) {
        Domicilio domicilioExistente = domicilioRepository.findById(id).orElse(null);
        if (domicilioExistente != null) {
            domicilioExistente.setCalle(nuevoDomicilio.getCalle());
            domicilioExistente.setNumero(nuevoDomicilio.getNumero());
            domicilioExistente.setCodigoPostal(nuevoDomicilio.getCodigoPostal());
            domicilioExistente.setLocalidad(nuevoDomicilio.getLocalidad());
            return domicilioRepository.save(domicilioExistente);
        }
        return null; 
    }

}
