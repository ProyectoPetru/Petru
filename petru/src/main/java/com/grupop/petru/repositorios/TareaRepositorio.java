
package com.grupop.petru.repositorios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Tarea;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea, String> {
    @Query("SELECT t FROM Tarea t WHERE t.proyecto.id = :id")
    public List<Tarea> getTareaByProyecto(@Param("id") String id);

    @Query("SELECT t FROM Tarea t JOIN t.proyecto p JOIN p.usuarios u WHERE u.id = :id")
    public List<Tarea> buscarPorUsuario(@Param("id")String id);
}
