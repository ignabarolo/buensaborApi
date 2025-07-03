
package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.Usuario;
import com.utn.buensaborApi.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsuarioServices {
     @Autowired
     private UsuarioRepository usuarioRepository;
     
    public List<Usuario> listarTodos(){
         return usuarioRepository.findAll();
     }
     
    public Usuario guardar (Usuario usuario){
         return usuarioRepository.save(usuario);
     }
     
    public Usuario obtenerPorId (Long id) {
         return usuarioRepository.findById(id).orElse(null);
     }
    public void eliminar (long id){
         usuarioRepository.deleteById(id);
     }   
     
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
    Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);
    if (usuarioExistente != null) {
        
        usuarioExistente.setAuth0id(usuarioActualizado.getAuth0id());
        usuarioExistente.setNombreUsuario(usuarioActualizado.getNombreUsuario());

        return usuarioRepository.save(usuarioExistente);
    }
    return null;
}

}
