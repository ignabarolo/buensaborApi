
package com.utn.buensaborApi.services;


import com.utn.buensaborApi.corsConfiguration.Auth0Service;
import com.utn.buensaborApi.Dtos.EmpleadoDTO;
import com.utn.buensaborApi.enums.Rol;
import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.models.Empleado;
import com.utn.buensaborApi.models.Usuario;
import com.utn.buensaborApi.repositories.SucursalEmpresaRepository;
import com.utn.buensaborApi.repositories.domicilioRepository;
import com.utn.buensaborApi.repositories.empleadoRepository;
import com.utn.buensaborApi.repositories.localidadRepository;
import com.utn.buensaborApi.repositories.usuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class empleadoServices {
@Autowired
private empleadoRepository empleadoRepository;
@Autowired
private Auth0Service auth0Service;

@Autowired
private usuarioRepository usuarioRepository;

@Autowired
private SucursalEmpresaRepository sucursalRepository;

@Autowired
private domicilioRepository domicilioRepository;
@Autowired
private localidadRepository localidadRepository;

private final Map<Rol, String> auth0RolesMap = Map.of(
    Rol.ADMINISTRACION, "rol_8HhYUQDeo2Ke5yHY",
    Rol.CAJERO, "rol_cu5roF7jenjQSpbk",
    Rol.COCINERO, "rol_wqYaKnyV3KyOOmAu",
    Rol.DELIVERY, "rol_yo9YdgX2TgEaGtzE"
);


     
     public List<Empleado> listarTodos(){
         return empleadoRepository.findAll();
     }
     
     public Empleado guardar (Empleado empleado){
         return empleadoRepository.save(empleado);
     }
     
     public Empleado obtenerPorId (Long id) {
         return empleadoRepository.findById(id).orElse(null);
     }

     public void eliminar(Long id) {
    Empleado empleado = obtenerPorId(id);
    if (empleado != null && empleado.getFechaBaja() == null) {
        empleado.setFechaBaja(LocalDateTime.now());
        empleadoRepository.save(empleado);
    }
}


public Empleado actualizarEmpleado(Long id, EmpleadoDTO dto) {
    Empleado empleadoExistente = empleadoRepository.findById(id).orElse(null);
    if (empleadoExistente != null) {
        empleadoExistente.setNombre(dto.getNombre());
        empleadoExistente.setApellido(dto.getApellido());
        empleadoExistente.setTelefono(dto.getTelefono());
        empleadoExistente.setEmail(dto.getEmail());

        // Actualizar rol en la entidad y en Auth0 si cambió
        Rol rolAnterior = empleadoExistente.getRol();
        Rol nuevoRol = dto.getRol();

        if (!rolAnterior.equals(nuevoRol)) {
            empleadoExistente.setRol(nuevoRol);
            String auth0Id = empleadoExistente.getUsuario().getAuth0id();
            String nuevoRolId = auth0RolesMap.get(nuevoRol);

            // Remover roles anteriores y asignar el nuevo
            auth0Service.removerTodosLosRoles(auth0Id);
            auth0Service.asignarRol(auth0Id, nuevoRolId);
        }

        // Actualizar domicilio (nuevo o existente)
        Domicilio domicilio = empleadoExistente.getDomicilio();
        if (domicilio == null) {
            domicilio = new Domicilio();
        }
        domicilio.setCalle(dto.getDomicilio().getCalle());
        domicilio.setNumero(dto.getDomicilio().getNumero());
        domicilio.setCodigoPostal(dto.getDomicilio().getCodigoPostal());
        domicilio.setLocalidad(
            localidadRepository.findById(dto.getDomicilio().getIdLocalidad())
                .orElseThrow(() -> new RuntimeException("Localidad no encontrada"))
        );
        domicilio = domicilioRepository.save(domicilio);
        empleadoExistente.setDomicilio(domicilio);

        // Actualizar sucursal
        empleadoExistente.setSucursal(
            sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"))
        );

        return empleadoRepository.save(empleadoExistente);
    }
    return null;
}



public Empleado crearEmpleadoConAuth0(EmpleadoDTO dto) {
    try {
        // 1. Crear el usuario en Auth0 y asignarle el rol
        String auth0id = auth0Service.crearUsuario(dto.getEmail(), dto.getNombre());
        String rolId = auth0RolesMap.get(dto.getRol());
        auth0Service.asignarRol(auth0id, rolId);

        // 2. Crear entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setAuth0id(auth0id);
        usuario.setNombreUsuario(dto.getEmail());
        usuario = usuarioRepository.save(usuario);

        // 3. Crear entidad Domicilio a partir del DomicilioDTO
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(dto.getDomicilio().getCalle());
        domicilio.setNumero(dto.getDomicilio().getNumero());
        domicilio.setCodigoPostal(dto.getDomicilio().getCodigoPostal());
        domicilio.setLocalidad(localidadRepository.findById(dto.getDomicilio().getIdLocalidad())
            .orElseThrow(() -> new RuntimeException("Localidad no encontrada")));
        domicilio = domicilioRepository.save(domicilio);

        // 4. Crear entidad Empleado
        Empleado empleado = new Empleado();
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setTelefono(dto.getTelefono());
        empleado.setEmail(dto.getEmail());
        empleado.setRol(dto.getRol());
        empleado.setUsuario(usuario);
        empleado.setSucursal(
            sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"))
        );
        empleado.setDomicilio(domicilio);

        return empleadoRepository.save(empleado);
    } catch (Exception e) {
        throw new RuntimeException("Error al crear el empleado: " + e.getMessage());
    }
}
 public Empleado obtenerPorEmail(String email) {
    return empleadoRepository.findByEmail(email).orElse(null);
}

}
