package com.grupop.petru.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class Token {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private Boolean usado = false;

    @ManyToOne
    private Usuario usuario;

    public Token() {
    }

    public Token(Usuario usuario) {
        this.usuario = usuario;
    }
}
