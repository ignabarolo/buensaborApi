
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Cliente;
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

     public void eliminar (long id){
         clienteRepository.deleteById(id);
     }

    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id).orElse(null);
        if (clienteExistente != null) {
            clienteExistente.setNombre(clienteActualizado.getNombre());
            clienteExistente.setApellido(clienteActualizado.getApellido());
            clienteExistente.setTelefono(clienteActualizado.getTelefono());
            clienteExistente.setEmail(clienteActualizado.getEmail());
            clienteExistente.setFechaDeNacimiento(clienteActualizado.getFechaDeNacimiento());
            clienteExistente.setUsuario(clienteActualizado.getUsuario());
            clienteExistente.setDomicilio(clienteActualizado.getDomicilio());
            //clienteExistente.setPedidosVenta(clienteActualizado.getPedidosVenta());
            //clienteExistente.setFacturas(clienteActualizado.getFacturas());
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
