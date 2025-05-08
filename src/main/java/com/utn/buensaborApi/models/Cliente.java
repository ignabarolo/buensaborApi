
package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;



/**
 *
 * @author Enzo
 */
@Entity
public class Cliente {
 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY)
 private int id_cliente; 
 
 private String nombre;
 private String apellido;
 private String telefono;
 private String email;
 private LocalDate fechaDeNacimiento;
 
 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
 private Usuario usuario;
 
 @OneToOne(cascade = CascadeType.ALL)
 @JoinColumn(name = "id_domicilio", referencedColumnName = "id_domicilio")
 private Domicilio domicilio;
 
 @OneToMany(mappedBy = "cliente")
 private List<pedidoVenta> PedidosVenta;
 
  @OneToMany(mappedBy = "cliente")
 private List<Factura> facturas;
}
