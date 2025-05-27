
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.repositories.domicilioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class domicilioServices {
     @Autowired
     private domicilioRepository domicilioRepository;
     
     public List<Domicilio> listarTodos(){
         return domicilioRepository.findAll();
     }
     
     public Domicilio guardar (Domicilio domicilio){
     
         domicilio.setFechaAlta(LocalDateTime.now());
         return domicilioRepository.save(domicilio);
     }
     
     public Domicilio obtenerPorId (Long id) {
         return domicilioRepository.findById(id).orElse(null);
     }
    public void eliminar(long id){
    Domicilio domicilio = domicilioRepository.findById(id).orElse(null);
    if (domicilio != null) {
        domicilio.setFechaBaja(LocalDateTime.now());
        domicilioRepository.save(domicilio);
    }
}

    public Domicilio actualizarDomicilio(Long id, Domicilio nuevoDomicilio) {
        Domicilio domicilioExistente = domicilioRepository.findById(id).orElse(null);
        if (domicilioExistente != null) {
            domicilioExistente.setCalle(nuevoDomicilio.getCalle());
            domicilioExistente.setNumero(nuevoDomicilio.getNumero());
            domicilioExistente.setCodigoPostal(nuevoDomicilio.getCodigoPostal());
            domicilioExistente.setLocalidad(nuevoDomicilio.getLocalidad());
            domicilioExistente.setFechaModificacion(LocalDateTime.now());
            return domicilioRepository.save(domicilioExistente);
        }
        return null; 
    }

}
