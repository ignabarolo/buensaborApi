
package com.utn.buensaborApi.models;
import jakarta.persistence.*;

/**
 *
 * @author Enzo
 */
@Entity
public class Domicilio {
    
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private long id_domicilio;

private String calle;
private int numero;
private int codigoPostal;


@ManyToOne
@JoinColumn(name = "id_localidad")
private Localidad localidad;

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public long getId_domicilio() {
        return id_domicilio;
    }

    public void setId_domicilio(long id_domicilio) {
        this.id_domicilio = id_domicilio;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }
}
