
package com.utn.buensaborApi.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.buensaborApi.enums.Rol;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;



@Entity
@Getter
@Setter
public class Empleado extends BaseEntity {

  private String nombre;
  private String apellido;
  private String telefono;
  private String email;

  @Enumerated(EnumType.STRING)
  private Rol rol;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_usuario", referencedColumnName = "id")
  private Usuario usuario;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "id_sucursal", referencedColumnName = "id")
  private SucursalEmpresa sucursal;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_domicilio", referencedColumnName = "id")
  private Domicilio domicilio;

  @JsonIgnore
  @OneToMany(mappedBy = "empleado")
  private List<PedidoVenta> pedidosVenta;

  @JsonIgnore
  private String password;  // nueva propiedad
}
