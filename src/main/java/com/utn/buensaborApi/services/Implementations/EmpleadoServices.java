package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.config.corsConfiguration.Auth0Service;
import com.utn.buensaborApi.dtos.Usuarios.EmpleadoDTO;
import com.utn.buensaborApi.enums.Rol;
import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.models.Empleado;
import com.utn.buensaborApi.models.Usuario;
import com.utn.buensaborApi.repositories.SucursalEmpresaRepository;
import com.utn.buensaborApi.repositories.DomicilioRepository;
import com.utn.buensaborApi.repositories.EmpleadoRepository;
import com.utn.buensaborApi.repositories.LocalidadRepository;
import com.utn.buensaborApi.repositories.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoServices {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private Auth0Service auth0Service;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SucursalEmpresaRepository sucursalRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Map<Rol, String> auth0RolesMap = Map.of(
        Rol.ADMINISTRACION, "rol_8HhYUQDeo2Ke5yHY",
        Rol.CAJERO, "rol_cu5roF7jenjQSpbk",
        Rol.COCINERO, "rol_wqYaKnyV3KyOOmAu",
        Rol.DELIVERY, "rol_yo9YdgX2TgEaGtzE"
    );

    public List<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }

    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public Empleado obtenerPorId(Long id) {
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

        // Solo si cambió el email
        if (!empleadoExistente.getEmail().equals(dto.getEmail())) {
            empleadoExistente.setEmail(dto.getEmail());

            // Actualizar email en Usuario
            Usuario usuario = empleadoExistente.getUsuario();
            usuario.setNombreUsuario(dto.getEmail());
            usuarioRepository.save(usuario);

            // Actualizar email en Auth0
            String auth0Id = usuario.getAuth0id();
            auth0Service.actualizarEmailYNombre(auth0Id, dto.getEmail(), dto.getNombre());
        }

        // Actualizar rol si cambió
        Rol rolAnterior = empleadoExistente.getRol();
        Rol nuevoRol = dto.getRol();
        if (!rolAnterior.equals(nuevoRol)) {
            empleadoExistente.setRol(nuevoRol);
            String auth0Id = empleadoExistente.getUsuario().getAuth0id();
            String nuevoRolId = auth0RolesMap.get(nuevoRol);

            auth0Service.removerTodosLosRoles(auth0Id);
            auth0Service.asignarRol(auth0Id, nuevoRolId);
        }

        // Domicilio
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

        // Sucursal
        empleadoExistente.setSucursal(
            sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"))
        );

        // Contraseña opcional
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            empleadoExistente.setPassword(encodedPassword);
        }

        return empleadoRepository.save(empleadoExistente);
    }
    return null;
}


   public Empleado crearEmpleadoConAuth0(EmpleadoDTO dto) {
        try {

            String auth0id = auth0Service.crearUsuario(dto.getEmail(), dto.getNombre(), dto.getPassword());
            String rolId = auth0RolesMap.get(dto.getRol());
            auth0Service.asignarRol(auth0id, rolId);


            Usuario usuario = new Usuario();
            usuario.setAuth0id(auth0id);
            usuario.setNombreUsuario(dto.getEmail());
            usuario = usuarioRepository.save(usuario);


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


            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            empleado.setPassword(encodedPassword);

            return empleadoRepository.save(empleado);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el empleado: " + e.getMessage());
        }
    }

    public Empleado obtenerPorEmail(String email) {
        return empleadoRepository.findByEmail(email).orElse(null);
    }
}

