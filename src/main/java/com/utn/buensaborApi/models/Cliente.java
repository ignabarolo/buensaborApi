
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
 
 //@OneToMany(mappedBy = "cliente")
 //private List<pedidoVenta> PedidosVenta;
 
//  @OneToMany(mappedBy = "cliente")
 //private List<Factura> facturas;


 public int getId_cliente() {
  return id_cliente;
 }

 public void setId_cliente(int id_cliente) {
  this.id_cliente = id_cliente;
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

 public LocalDate getFechaDeNacimiento() {
  return fechaDeNacimiento;
 }

 public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
  this.fechaDeNacimiento = fechaDeNacimiento;
 }

 public Usuario getUsuario() {
  return usuario;
 }

 public void setUsuario(Usuario usuario) {
  this.usuario = usuario;
 }

 public Domicilio getDomicilio() {
  return domicilio;
 }

 public void setDomicilio(Domicilio domicilio) {
  this.domicilio = domicilio;
 }

 /*public List<pedidoVenta> getPedidosVenta() {
  return PedidosVenta;
 }

 public void setPedidosVenta(List<pedidoVenta> pedidosVenta) {
  PedidosVenta = pedidosVenta;
 }

 public List<Factura> getFacturas() {
  return facturas;
 }

 public void setFacturas(List<Factura> facturas) {
  this.facturas = facturas;
 }*/
}
