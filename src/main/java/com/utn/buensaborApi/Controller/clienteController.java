package com.utn.buensaborApi.Controller;


import com.utn.buensaborApi.Dtos.clienteDTO;
import com.utn.buensaborApi.Dtos.domicilioDTO;
import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.services.ClienteService;
import com.utn.buensaborApi.services.localidadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")

public class clienteController {

    @Autowired
    private ClienteService clienteServices;
    @Autowired
    private localidadServices localidadServices;

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodosLosClientes() {
        return ResponseEntity.ok(clienteServices.listarTodos());
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
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteActualizado) {
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

}

