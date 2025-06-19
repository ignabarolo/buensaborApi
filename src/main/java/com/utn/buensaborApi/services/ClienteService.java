
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.Dtos.domicilioDTO;
import com.utn.buensaborApi.Dtos.putClienteDTO;
import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.repositories.clienteRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
     @Autowired
     private clienteRepository clienteRepository;
     
     @Autowired
     private localidadServices localidadServices;
     
     public List<Cliente> listarTodos(){
         return clienteRepository.findAll();
     }
     
     public Cliente guardar (Cliente cliente){
         cliente.setFechaAlta(LocalDateTime.now());
         return clienteRepository.save(cliente);
     }
     
     public Cliente obtenerPorId (Long id) {
         return clienteRepository.findById(id).orElse(null);
     }

      public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        if (cliente != null && cliente.getFechaBaja() == null) {
            cliente.setFechaBaja(LocalDateTime.now());
            clienteRepository.save(cliente);
        }
    }
      

  public Cliente actualizar(Long id, putClienteDTO dto) {
    Cliente clienteExistente = clienteRepository.findById(id).orElse(null);
    if (clienteExistente != null) {
        clienteExistente.setNombre(dto.getNombre());
        clienteExistente.setApellido(dto.getApellido());
        clienteExistente.setTelefono(dto.getTelefono());
        clienteExistente.setEmail(dto.getEmail());
        clienteExistente.setFechaDeNacimiento(dto.getFechaDeNacimiento());

        // Domicilio
        domicilioDTO domicilioDto = dto.getDomicilio();
        Domicilio domicilio = clienteExistente.getDomicilio() != null ? clienteExistente.getDomicilio() : new Domicilio();
        domicilio.setCalle(domicilioDto.getCalle());
        domicilio.setNumero(domicilioDto.getNumero());
        domicilio.setCodigoPostal(domicilioDto.getCodigoPostal());

        Localidad localidad = localidadServices.obtenerPorId(domicilioDto.getIdLocalidad());
        if (localidad != null) {
            domicilio.setLocalidad(localidad);
        }

        clienteExistente.setDomicilio(domicilio);
        clienteExistente.setFechaModificacion(LocalDateTime.now());
        return clienteRepository.save(clienteExistente);
    }
    return null;
}
    public Cliente obtenerPorEmail(String email) {
    return clienteRepository.findByEmail(email).orElse(null);
}
}
