
package com.utn.buensaborApi.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.utn.buensaborApi.models.Empleado;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface empleadoRepository extends JpaRepository <Empleado , Long> {
  
 
}
