package com.grupop.petru.servicios;

import com.grupop.petru.entidades.Comentario;
import com.grupop.petru.entidades.Etiqueta;
import com.grupop.petru.entidades.Tarea;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.ComentarioRepositorio;
import com.grupop.petru.repositorios.TareaRepositorio;
import com.grupop.petru.repositorios.UsuarioRepositorio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @authors Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *          Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *          Manuel Dominich Martinez - Maximo Carbonetti
 *          Salvador Caldarella - Sebastián A. Petrini
 */

@Service
public class ComentarioServicio {
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private TareaRepositorio tareaRepositorio;

    // -------------------------------------CREAR-----------------------------------------------------//
    @Transactional
    public void crearComentario(String idTarea, String idUsuario, String contenido) throws MiException {
        Tarea tarea = tareaRepositorio.getOne(idTarea);
        Usuario usuario = usuarioRepositorio.getOne(idUsuario);

        Comentario comentario = new Comentario();
        
        comentario.setContenido(contenido);
        comentario.setFecha(new Date());
        comentario.setUsuario(usuario);

        comentario = comentarioRepositorio.save(comentario);

        tarea.addComentario(comentario);

        tareaRepositorio.save(tarea);
    }

    @Transactional
    public void borrarComentario(String id) {
        Comentario comentario = comentarioRepositorio.getOne(id);

        Tarea tarea = comentarioRepositorio.getTareaComentario(id);

        tarea.getComentarios().remove(comentario);

        tareaRepositorio.save(tarea);

        comentarioRepositorio.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Comentario listarPorId(String id) throws MiException {
        Optional<Comentario> comentarioRes = comentarioRepositorio.findById(id);

        if (comentarioRes.isPresent()) {
            return comentarioRes.get();
        }
        
        throw new MiException("Comentario no encontrado");
    }
}
