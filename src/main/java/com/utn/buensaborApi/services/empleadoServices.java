
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Empleado;
import com.utn.buensaborApi.repositories.empleadoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Enzo
 */
@Service
public class empleadoServices {
           @Autowired
     private empleadoRepository empleadoRepository;
     
     public List<Empleado> listarTodos(){
         return empleadoRepository.findAll();
     }
     
     public Empleado guardar (Empleado empleado){
         return empleadoRepository.save(empleado);
     }
     
     public Empleado obtenerPorId (Long id) {
         return empleadoRepository.findById(id).orElse(null);
     }

     public void eliminar (long id){
         empleadoRepository.deleteById(id);
     }

    public Empleado actualizarEmpleado(Long id, Empleado nuevoEmpleado) {
        Empleado empleadoExistente = empleadoRepository.findById(id).orElse(null);
        if (empleadoExistente != null) {
            empleadoExistente.setNombre(nuevoEmpleado.getNombre());
            empleadoExistente.setApellido(nuevoEmpleado.getApellido());
            empleadoExistente.setTelefono(nuevoEmpleado.getTelefono());
            empleadoExistente.setEmail(nuevoEmpleado.getEmail());
            empleadoExistente.setRol(nuevoEmpleado.getRol());

            // Relaciones
            empleadoExistente.setUsuario(nuevoEmpleado.getUsuario());
            empleadoExistente.setSucursal(nuevoEmpleado.getSucursal());
            empleadoExistente.setDomicilio(nuevoEmpleado.getDomicilio());

            return empleadoRepository.save(empleadoExistente);
        }
        return null;
    }

}
