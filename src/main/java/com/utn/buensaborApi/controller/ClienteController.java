package com.utn.buensaborApi.controller;


import com.utn.buensaborApi.dtos.Usuarios.clienteDTO;
import com.utn.buensaborApi.dtos.Usuarios.domicilioDTO;
import com.utn.buensaborApi.dtos.Usuarios.putClienteDTO;
import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.dtos.Pedido.ClientePedidoDto;
import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.models.PedidoVenta;
import com.utn.buensaborApi.services.Implementations.ClienteService;
import com.utn.buensaborApi.services.Implementations.PedidoVentaServiceImpl;
import com.utn.buensaborApi.services.Implementations.LocalidadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")

public class ClienteController {

    @Autowired
    private ClienteService clienteServices;
    @Autowired
    private LocalidadServices localidadServices;
    @Autowired
    private PedidoVentaServiceImpl pedidoVentaService;

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodosLosClientes() {
        return ResponseEntity.ok(clienteServices.listarTodos());
    }
    @GetMapping("/auth0/{auth0Id}")
public ResponseEntity<Cliente> obtenerPorAuth0Id(@PathVariable String auth0Id) {
    Cliente cliente = clienteServices.obtenerPorAuth0Id(auth0Id);
    if (cliente != null) {
        return ResponseEntity.ok(cliente);
    } else {
        return ResponseEntity.notFound().build();
    }
}


    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteServices.obtenerPorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

   @PutMapping("/{id}/reactivar")
  public ResponseEntity<?> reactivarCliente(@PathVariable Long id) {
    Cliente cliente = clienteServices.obtenerPorId(id);
    if (cliente == null) {
        return ResponseEntity.notFound().build();
    }

    cliente.setFechaBaja(null);
    clienteServices.guardar(cliente); 

    return ResponseEntity.ok(cliente);
}

    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody clienteDTO clienteDto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setTelefono(clienteDto.getTelefono());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setFechaDeNacimiento(clienteDto.getFechaDeNacimiento());
        cliente.setUsuario(clienteDto.getUsuario());

        // Convertir DomicilioDTO
        domicilioDTO domicilioDto = clienteDto.getDomicilio();
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(domicilioDto.getCalle());
        domicilio.setNumero(domicilioDto.getNumero());
        domicilio.setCodigoPostal(domicilioDto.getCodigoPostal());

        Localidad localidad = localidadServices.obtenerPorId(domicilioDto.getIdLocalidad());
        if (localidad == null) {
            return ResponseEntity.badRequest().body("Localidad no encontrada");
        }

        domicilio.setLocalidad(localidad);
        cliente.setDomicilio(domicilio);

        Cliente clienteGuardado = clienteServices.guardar(cliente);
        return ResponseEntity.ok(clienteGuardado);
    }



   @PutMapping("/{id}")
public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody putClienteDTO clienteActualizado) {
    Cliente cliente = clienteServices.actualizar(id, clienteActualizado);
    if (cliente != null) {
        return ResponseEntity.ok(cliente);
    } else {
        return ResponseEntity.notFound().build();  // Si no se encuentra el cliente, se retorna 404
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        Cliente cliente = clienteServices.obtenerPorId(id);
        if (cliente != null) {
            clienteServices.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> obtenerClientePorEmail(@PathVariable String email) {
    Cliente cliente = clienteServices.obtenerPorEmail(email);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/por-pedido/{idPedido}")
    public ResponseEntity<?> obtenerClientePorPedido(@PathVariable Long idPedido) {
        try {
            // Buscar el pedido por su ID
            PedidoVenta pedido = pedidoVentaService.findById(idPedido);

            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"No se encontró el pedido con el ID proporcionado.\"}");
            }

            // Obtener el cliente asociado al pedido
            Cliente cliente = pedido.getCliente();

            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"El pedido no tiene un cliente asociado.\"}");
            }

            // Convertir a DTO
            ClientePedidoDto clienteDto = clienteServices.toDto(cliente);

            return ResponseEntity.ok(clienteDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}