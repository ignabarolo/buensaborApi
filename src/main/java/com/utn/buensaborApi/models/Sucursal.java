
package com.utn.buensaborApi.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
/**
 *
 * @author Enzo
 *
 */
@Entity
public class Sucursal {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id_sucursal;  
   
   private String nombre;
   private String horaApertura;
   private String horaCierre;
   
    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    
    @OneToMany(mappedBy = "sucursal")
    @JsonIgnore
    private List<Empleado> empleados;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_domicilio", referencedColumnName = "id_domicilio")
    private Domicilio domicilio;
    
   /* @OneToMany(mappedBy = "sucursal")
    private List<Factura> facturas;
    
    @OneToMany(mappedBy = "sucursal")
    private List<pedidoVenta> pedidosVenta;
    
    @OneToMany(mappedBy = "sucursal")
    private List<Promocion> promociones;
    
    @OneToMany(mappedBy = "sucursal")
    private List<Articulo> articulos;
    
    @OneToMany(mappedBy = "sucursal")
    private List<sucursalInsumos> sucursalInsumos;
    
    @OneToMany(mappedBy = "sucursal")
    private List<categoriaArticulo> categoriasArticulo;*/

    public Long getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(Long id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura) {
        this.horaApertura = horaApertura;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }
/*
    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public List<pedidoVenta> getPedidosVenta() {
        return pedidosVenta;
    }

    public void setPedidosVenta(List<pedidoVenta> pedidosVenta) {
        this.pedidosVenta = pedidosVenta;
    }

    public List<Promocion> getPromociones() {
        return promociones;
    }

    public void setPromociones(List<Promocion> promociones) {
        this.promociones = promociones;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public List<sucursalInsumos> getSucursalInsumos() {
        return sucursalInsumos;
    }

    public void setSucursalInsumos(List<sucursalInsumos> sucursalInsumos) {
        this.sucursalInsumos = sucursalInsumos;
    }

    public List<categoriaArticulo> getCategoriasArticulo() {
        return categoriasArticulo;
    }

    public void setCategoriasArticulo(List<categoriaArticulo> categoriasArticulo) {
        this.categoriasArticulo = categoriasArticulo;
    }*/
}
