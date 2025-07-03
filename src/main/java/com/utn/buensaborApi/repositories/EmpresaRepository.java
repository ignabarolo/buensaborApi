
package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmpresaRepository extends JpaRepository <Empresa , Long>{
    
}
