package com.utn.buensaborApi.repository;

import com.utn.buensaborApi.models.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface BaseRepository <E extends BaseEntity, ID extends Serializable> extends JpaRepository <E, ID> {
}
