
package com.utn.buensaborApi.models;
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
    private List<Empleado> empleados;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_domicilio", referencedColumnName = "id_domicilio")
    private Domicilio domicilio;
    
    @OneToMany(mappedBy = "sucursal")
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
    private List<categoriaArticulo> categoriasArticulo;
    
}
