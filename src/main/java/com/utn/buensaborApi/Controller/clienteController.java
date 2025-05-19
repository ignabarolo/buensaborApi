package com.utn.buensaborApi.Controller;



import com.utn.buensaborApi.models.Cliente;
import com.utn.buensaborApi.services.clienteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class clienteController {

    @Autowired
    private clienteServices clienteServices;

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
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteServices.guardar(cliente);
        return ResponseEntity.ok(nuevoCliente);
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
}

