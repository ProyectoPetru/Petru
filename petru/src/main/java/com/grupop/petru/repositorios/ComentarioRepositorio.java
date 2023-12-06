
package com.grupop.petru.repositorios;

import com.grupop.petru.entidades.Comentario;
import com.grupop.petru.entidades.Tarea;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, String> {
    @Query("SELECT t FROM Tarea t JOIN t.comentarios c WHERE c.id = :id")
    public Tarea getTareaComentario(@Param("id") String id);
}
