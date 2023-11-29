package com.grupop.petru.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grupop.petru.entidades.Token;
import com.grupop.petru.entidades.Usuario;

public interface TokenRepositorio  extends JpaRepository<Token, String>{
    @Query("SELECT u FROM Usuario u WHERE u.id = (SELECT t.usuario.id FROM Token t WHERE t.id = :id)")
    public Usuario getByToken(@Param("id") String id);
}
