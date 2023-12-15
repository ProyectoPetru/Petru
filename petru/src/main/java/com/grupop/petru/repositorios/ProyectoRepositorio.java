
package com.grupop.petru.repositorios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Proyecto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, String>{
    
    @Query("SELECT p FROM Proyecto p JOIN p.usuarios u WHERE u.id = :id")
    public List<Proyecto> buscarPorUsuario(@Param("id")String id);

    @Query("SELECT p FROM Proyecto p WHERE p.nombre LIKE %:nombre%")
    public List<Proyecto> buscarPorNombre(@Param("nombre")String nombre);

    @Query("SELECT p FROM Proyecto p WHERE p.notas LIKE %:notas%")
    public List<Proyecto> buscarPorNotas(@Param("notas")String notas);

}
