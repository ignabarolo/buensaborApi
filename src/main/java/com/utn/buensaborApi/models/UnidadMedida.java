package com.utn.buensaborApi.models;

import com.utn.buensaborApi.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UnidadMedida extends BaseEntity {
    private String denominacion;
}