
package com.utn.buensaborApi.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
public  class SucursalEmpresa extends BaseEntity{
   
   
   private String nombre;
   private String horaApertura;
   private String horaCierre;
   
    @ManyToOne
    @JoinColumn(name = "id_empresa", referencedColumnName = "id")
    private Empresa empresa;
    
    @OneToMany(mappedBy = "sucursal")
    @JsonIgnore
   private List<Empleado> empleados;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_domicilio", referencedColumnName = "id")
    private Domicilio domicilio;
    
   // @OneToMany(mappedBy = "sucursal")
   // private List<Factura> facturas;
    
    //@OneToMany(mappedBy = "sucursal")
    //private List<pedidoVenta> pedidosVenta;
    
   // @OneToMany(mappedBy = "sucursal")
    //private List<Promocion> promociones;
    
    @OneToMany(mappedBy = "sucursal")
    private List<Articulo> articulos;
    
    @OneToMany(mappedBy = "sucursal")
    private List<SucursalInsumo> sucursalInsumos;
    
    @OneToMany(mappedBy = "sucursal")
    private List<CategoriaArticulo> categoriasArticulo;


}
