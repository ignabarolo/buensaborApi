
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

 public long getId_empleado() {
  return id_empleado;
 }

 public void setId_empleado(long id_empleado) {
  this.id_empleado = id_empleado;
 }

 public String getNombre() {
  return nombre;
 }

 public void setNombre(String nombre) {
  this.nombre = nombre;
 }

 public String getApellido() {
  return apellido;
 }

 public void setApellido(String apellido) {
  this.apellido = apellido;
 }

 public String getTelefono() {
  return telefono;
 }

 public void setTelefono(String telefono) {
  this.telefono = telefono;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }

 public Rol getRol() {
  return rol;
 }

 public void setRol(Rol rol) {
  this.rol = rol;
 }

 public Usuario getUsuario() {
  return usuario;
 }

 public void setUsuario(Usuario usuario) {
  this.usuario = usuario;
 }

 public Sucursal getSucursal() {
  return sucursal;
 }

 public void setSucursal(Sucursal sucursal) {
  this.sucursal = sucursal;
 }

 public Domicilio getDomicilio() {
  return domicilio;
 }

 public void setDomicilio(Domicilio domicilio) {
  this.domicilio = domicilio;
 }

 public List<pedidoVenta> getPedidosVenta() {
  return pedidosVenta;
 }

 public void setPedidosVenta(List<pedidoVenta> pedidosVenta) {
  this.pedidosVenta = pedidosVenta;
 }
}
