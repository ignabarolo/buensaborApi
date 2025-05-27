
package com.utn.buensaborApi.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.utn.buensaborApi.models.Cliente;
/**
 *
 * @author Enzo
 */
public interface clienteRepository extends JpaRepository <Cliente , Long>{
    
}
