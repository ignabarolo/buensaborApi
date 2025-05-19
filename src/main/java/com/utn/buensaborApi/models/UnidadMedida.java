package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UnidadMedida extends BaseEntity {
    private String denominacion;
}