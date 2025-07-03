
package com.utn.buensaborApi.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.buensaborApi.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class Domicilio extends BaseEntity {
    
    
private String calle;
private int numero;
private int codigoPostal;

@JsonIgnore
@OneToOne(mappedBy = "domicilio")
private SucursalEmpresa sucursal;


@ManyToOne
@JoinColumn(name = "id_localidad", referencedColumnName = "id")
private Localidad localidad;


}
