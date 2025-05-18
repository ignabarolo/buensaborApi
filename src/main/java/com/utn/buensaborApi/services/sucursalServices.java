
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Sucursal;
import com.utn.buensaborApi.repository.sucursalRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class sucursalServices {
             @Autowired
     private sucursalRepository sucursalRepository;
     
     public List<Sucursal> listarTodos(){
         return sucursalRepository.findAll();
     }
     
     public Sucursal guardar (Sucursal sucursal){
         return sucursalRepository.save(sucursal);
     }
     
     public Sucursal obtenerPorId (Long id) {
         return sucursalRepository.findById(id).orElse(null);
     }

     public void eliminar (long id){
         sucursalRepository.deleteById(id);
     }


    public Sucursal actualizar(Long id, Sucursal sucursalActualizada) {
        Sucursal sucursalExistente = obtenerPorId(id);
        if (sucursalExistente != null) {

            sucursalExistente.setNombre(sucursalActualizada.getNombre());
            sucursalExistente.setHoraApertura(sucursalActualizada.getHoraApertura());
            sucursalExistente.setHoraCierre(sucursalActualizada.getHoraCierre());
            sucursalExistente.setEmpresa(sucursalActualizada.getEmpresa());
            sucursalExistente.setDomicilio(sucursalActualizada.getDomicilio());
            sucursalExistente.setEmpleados(sucursalActualizada.getEmpleados());
           // sucursalExistente.setFacturas(sucursalActualizada.getFacturas());
           //sucursalExistente.setPedidosVenta(sucursalActualizada.getPedidosVenta());
            //sucursalExistente.setPromociones(sucursalActualizada.getPromociones());
            //sucursalExistente.setArticulos(sucursalActualizada.getArticulos());
            //sucursalExistente.setSucursalInsumos(sucursalActualizada.getSucursalInsumos());
            //sucursalExistente.setCategoriasArticulo(sucursalActualizada.getCategoriasArticulo());


            return sucursalRepository.save(sucursalExistente);
        } else {
            return null;
        }
    }
}
