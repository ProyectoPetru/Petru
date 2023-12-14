
package com.grupop.petru.servicios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Evento;
import com.grupop.petru.entidades.Proyecto;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.EventoRepositorio;
import com.grupop.petru.repositorios.ProyectoRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoServicio {

    @Autowired
    private EventoRepositorio eventoRepositorio;
    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Transactional
    public void crearEvento(String titulo, Date fecha_inicio, Date fecha_fin, String descripcion, String idProyecto)
            throws MiException {

        validar(titulo, fecha_inicio, fecha_fin, descripcion, idProyecto);

        Optional<Proyecto> respuestaProyecto = proyectoRepositorio.findById(idProyecto);

        Proyecto proyecto = new Proyecto();

        if (respuestaProyecto.isPresent()) {
            proyecto = respuestaProyecto.get();
        }
        Evento evento = new Evento();

        evento.setTitulo(titulo);
        evento.setDescripcion(descripcion);
        evento.setFecha_inicio(fecha_inicio);
        evento.setFecha_fin(fecha_fin);

        evento.setProyecto(proyecto);
        eventoRepositorio.save(evento);

    }

    @Transactional
    public void modificarEvento(String id, String titulo, Date fecha_inicio, Date fecha_fin, String descripcion,
            String idProyecto) throws MiException {

        validar(id, titulo, fecha_inicio, fecha_fin, descripcion, idProyecto);

        Optional<Evento> respuestaEvento = eventoRepositorio.findById(id);

        Optional<Proyecto> respuestaProyecto = proyectoRepositorio.findById(id);

        Proyecto proyecto = new Proyecto();

        if (respuestaProyecto.isPresent()) {
            proyecto = respuestaProyecto.get();
        }

        if (respuestaEvento.isPresent()) {
            Evento evento = respuestaEvento.get();

            evento.setTitulo(titulo);
            evento.setDescripcion(descripcion);
            evento.setFecha_inicio(fecha_inicio);
            evento.setFecha_fin(fecha_fin);
            evento.setProyecto(proyecto);

            eventoRepositorio.save(evento);
        }
    }

    public List<Evento> listarEventos() {

        List<Evento> eventos = new ArrayList();

        eventos = eventoRepositorio.findAll();

        return eventos;
    }

    public Evento getOne(String id) {
        return eventoRepositorio.getOne(id);
    }

    @Transactional
    public void eliminarEvento(String id) {
        Optional<Evento> respuestaEvento = eventoRepositorio.findById(id);

        if (respuestaEvento.isPresent()) {
            eventoRepositorio.deleteById(id);
        }

    }

    private void validar(String titulo, Date fecha_inicio, Date fecha_fin, String descripcion, String idProyecto)
            throws MiException {

        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("nombre no puede ser nulo");
        }
        if (fecha_inicio == null) {
            throw new MiException("fecha no puede ser nula");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiException("link no puede ser nulo");
        }
        if (idProyecto.isEmpty() || idProyecto == null) {
            throw new MiException("Id proyecto es nulo");
        }
    }

    private void validar(String id, String titulo, Date fecha_inicio, Date fecha_fin, String descripcion,
            String idProyecto) throws MiException {

        if (id == null) {
            throw new MiException("el id no puede ser nulo");
        }
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("nombre no puede ser nulo");
        }
        if (fecha_inicio == null) {
            throw new MiException("fecha no puede ser nula");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiException("link no puede ser nulo");
        }
        if (idProyecto.isEmpty() || idProyecto == null) {
            throw new MiException("Id proyecto es nulo");
        }
    }

}