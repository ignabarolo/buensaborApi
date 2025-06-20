
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.Dtos.domicilioDTO;
import com.utn.buensaborApi.Dtos.putClienteDTO;
import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.models.Dtos.Pedido.ClientePedidoDto;
import com.utn.buensaborApi.models.Dtos.Pedido.DomicilioDto;
import com.utn.buensaborApi.models.Dtos.Pedido.LocalidadDto;
import com.utn.buensaborApi.repositories.clienteRepository;
import java.time.LocalDateTime;
import java.util.List;

import com.utn.buensaborApi.services.Implementations.PedidoVentaServiceImpl;
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


    public ClientePedidoDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClientePedidoDto dto = new ClientePedidoDto();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());

        // Convertir el domicilio si existe
        if (cliente.getDomicilio() != null) {
            DomicilioDto domicilioDto = new DomicilioDto();
            domicilioDto.setCalle(cliente.getDomicilio().getCalle());
            domicilioDto.setNumero(cliente.getDomicilio().getNumero());
            domicilioDto.setCodigoPostal(cliente.getDomicilio().getCodigoPostal());

            // Si necesitas incluir la localidad
            if (cliente.getDomicilio().getLocalidad() != null) {
                LocalidadDto localidadDto = new LocalidadDto();
                localidadDto.setId(cliente.getDomicilio().getLocalidad().getId());
                localidadDto.setNombre(cliente.getDomicilio().getLocalidad().getNombre());
                domicilioDto.setLocalidad(localidadDto);
            }

            dto.setDomicilio(domicilioDto);
        }

        return dto;
    }
}
