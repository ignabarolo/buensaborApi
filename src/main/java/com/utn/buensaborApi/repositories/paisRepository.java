
package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Enzo
 */
public interface paisRepository extends JpaRepository <Pais, Long>{
    
}
