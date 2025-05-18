
package com.utn.buensaborApi.models;
import com.utn.buensaborApi.enums.Rol;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
/**
 *
 * @author Enzo
 */

@Entity
@Getter
@Setter

public abstract class Empleado extends BaseEntity {
 
 private String nombre;
 private String apellido;
 private String telefono;
 private String email;
 
 @Enumerated(EnumType.STRING)
 private Rol rol;
 
 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "id_usuario", referencedColumnName = "id")
 private Usuario usuario;
 
 @ManyToOne
 @JoinColumn(name = "id_sucursal", referencedColumnName = "id") 
 private SucursalEmpresa sucursal;
 
 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "id_domicilio", referencedColumnName = "id")
 private Domicilio domicilio;
 
//@OneToMany(mappedBy = "empleado")
 //private List<pedidoVenta> pedidosVenta;


}
