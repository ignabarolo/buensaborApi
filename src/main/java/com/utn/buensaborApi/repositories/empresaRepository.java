
package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author User
 */
public interface empresaRepository extends JpaRepository <Empresa , Long>{
    
}
