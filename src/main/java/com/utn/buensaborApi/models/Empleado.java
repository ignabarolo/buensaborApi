
package com.utn.buensaborApi.models;
import com.utn.buensaborApi.enums.Rol;
import jakarta.persistence.*;
import java.util.List;

/**
 *
 * @author Enzo
 */

@Entity
public class Empleado {
 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY) 

 private long id_empleado;
 
 private String nombre;
 private String apellido;
 private String telefono;
 private String email;
 
 @Enumerated(EnumType.STRING)
 private Rol rol;
 
 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
 private Usuario usuario;
 
 @ManyToOne
 @JoinColumn(name = "id_sucursal") 
 private Sucursal sucursal;
 
 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "id_domicilio", referencedColumnName = "id_domicilio")
 private Domicilio domicilio;
 
@OneToMany(mappedBy = "empleado")
 private List<pedidoVenta> pedidosVenta;
 
}
