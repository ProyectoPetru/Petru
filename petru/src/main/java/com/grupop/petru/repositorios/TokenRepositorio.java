package com.grupop.petru.repositorios;

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
import com.grupop.petru.entidades.Token;
import com.grupop.petru.entidades.Usuario;

public interface TokenRepositorio  extends JpaRepository<Token, String>{
    @Query("SELECT u FROM Usuario u WHERE u.id = (SELECT t.usuario.id FROM Token t WHERE t.id = :id)")
    public Usuario getByToken(@Param("id") String id);
}
