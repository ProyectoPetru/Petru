
package com.grupop.petru.repositorios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.ListasDashboard;
import com.grupop.petru.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email")String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'CLIENTE'")
    public List<Usuario> buscarClientes();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'COLABORADOR'")
    public List<Usuario> buscarColaborador();

    @Query("SELECT u.nombre FROM Usuario u")
    public List<String> listaNombre();
    
   @Query(value = "select u.nombre AS nombre "
                      + "from usuario u join proyecto_usuarios pu on u.id = pu.usuarios_id "
                      + "where (u.rol = 'COLABORADOR' or u.rol = 'ADMIN' ) "
                      + "group by u.nombre", nativeQuery = true)
    public List<String> listaUsuariosProyecto1();
    
    @Query(value = "select count(pu.proyecto_id) AS dato "
                      + "from usuario u join proyecto_usuarios pu on u.id = pu.usuarios_id "
                      + "where (u.rol = 'COLABORADOR' or u.rol = 'ADMIN' ) "
                      + "group by u.nombre", nativeQuery = true)
    public List<Long> listaUsuariosProyecto2();
      
}
