
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Empresa;
import com.utn.buensaborApi.repository.empresaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class empresaServices {
            @Autowired
     private empresaRepository empresaRepository;
     
     public List<Empresa> listarTodos(){
         return empresaRepository.findAll();
     }
     
     public Empresa guardar (Empresa empresa){
         return empresaRepository.save(empresa);
     }
     
     public Empresa obtenerPorId (Long id) {
         return empresaRepository.findById(id).orElse(null);
     }
     public void eliminar (long id){
         empresaRepository.deleteById(id);
     }

    public Empresa actualizarEmpresa(Long id, Empresa empresaActualizada) {
        Empresa empresaExistente = empresaRepository.findById(id).orElse(null);

        if (empresaExistente != null) {
            empresaExistente.setNombre(empresaActualizada.getNombre());
            empresaExistente.setRazonSocial(empresaActualizada.getRazonSocial());
            empresaExistente.setCuil(empresaActualizada.getCuil());

            return empresaRepository.save(empresaExistente);
        } else {
            return null;
        }
    }

}
