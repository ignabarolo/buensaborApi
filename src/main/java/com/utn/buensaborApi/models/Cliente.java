
package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.*;


@Entity
@Getter
@Setter
public class Cliente extends BaseEntity {

 
 private String nombre;
 private String apellido;
 private String telefono;
 private String email;
 private LocalDate fechaDeNacimiento;
 
 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "id_usuario", referencedColumnName = "id")
 private Usuario usuario;
 
 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "id_domicilio", referencedColumnName = "id")
 private Domicilio domicilio;
 
 @JsonIgnore
 @OneToMany(mappedBy = "cliente")
 protected List<PedidoVenta> PedidosVenta;
 @JsonIgnore
  @OneToMany(mappedBy = "cliente")
 protected List<Factura> facturas;

}
