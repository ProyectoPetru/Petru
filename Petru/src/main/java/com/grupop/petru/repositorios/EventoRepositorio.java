
package com.grupop.petru.repositorios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepositorio extends JpaRepository<Evento, String>{

}
