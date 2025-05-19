
package com.utn.buensaborApi.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
public class Empresa extends BaseEntity {

 
 private String nombre;
 private String razonSocial;
 private int cuil;
 
 @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
 @JsonIgnore
 private List<SucursalEmpresa> sucursales;



}
