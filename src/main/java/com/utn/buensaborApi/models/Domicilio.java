
package com.utn.buensaborApi.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class Domicilio extends BaseEntity{
    
    
private String calle;
private int numero;
private int codigoPostal;

@OneToOne(mappedBy = "domicilio")
private SucursalEmpresa sucursal;


@ManyToOne
@JoinColumn(name = "id_localidad", referencedColumnName = "id")
private Localidad localidad;


}
