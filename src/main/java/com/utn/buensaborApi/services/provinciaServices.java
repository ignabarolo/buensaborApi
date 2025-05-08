/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Provincia;
import com.utn.buensaborApi.repository.provinciaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class provinciaServices {
    @Autowired
     private provinciaRepository provinciaRepository;
     
     public List<Provincia> listarTodos(){
         return provinciaRepository.findAll();
     }
     
     public Provincia guardar (Provincia provincia){
         return provinciaRepository.save(provincia);
     }
     
     public Provincia obtenerPorId (Long id) {
         return provinciaRepository.findById(id).orElse(null);
     }
     public void eliminar (long id){
         provinciaRepository.deleteById(id);
     }
}
