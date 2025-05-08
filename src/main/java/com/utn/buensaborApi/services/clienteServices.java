
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.repository.clienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Enzo
 */
@Service
public class clienteServices {
     @Autowired
     
     private clienteRepository clienteRepository;
     
     public List<Cliente> listarTodos(){
         return clienteRepository.findAll();
     }
     
     public Cliente guardar (Cliente cliente){
         return clienteRepository.save(cliente);
     }
     
     public Cliente obtenerPorId (Long id) {
         return clienteRepository.findById(id).orElse(null);
     }
     public void eliminar (long id){
         clienteRepository.deleteById(id);
     } 
}
