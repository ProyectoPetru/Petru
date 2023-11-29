package com.grupop.petru.servicios;

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
    public void crearEvento(String id, String nombre, Date fecha, String link, String idProyecto) throws MiException {

        validar(id, nombre, fecha, link, idProyecto);

        Optional<Proyecto> respuestaProyecto = proyectoRepositorio.findById(idProyecto);

        Proyecto proyecto = new Proyecto();

        if (respuestaProyecto.isPresent()) {
            proyecto = respuestaProyecto.get();
        }
        Evento evento = new Evento();
        evento.setId(id);
        evento.setNombre(nombre);
        evento.setLink(link);
        evento.setFecha(fecha);
        evento.setProyecto(proyecto);
        eventoRepositorio.save(evento);

    }

    @Transactional
    public void modificarEvento(String id, String nombre, Date fecha, String link, String idProyecto) throws MiException {

        validar(id, nombre, fecha, link, idProyecto);

        Optional<Evento> respuestaEvento = eventoRepositorio.findById(id);

        Optional<Proyecto> respuestaProyecto = proyectoRepositorio.findById(id);

        Proyecto proyecto = new Proyecto();

        if (respuestaProyecto.isPresent()) {
            proyecto = respuestaProyecto.get();
        }

        if (respuestaEvento.isPresent()) {
            Evento evento = respuestaEvento.get();
            evento.setNombre(nombre);
            evento.setFecha(fecha);
            evento.setLink(link);
            evento.setProyecto(proyecto);
            eventoRepositorio.save(evento);
        }
    }

    public List<Evento> listarEventos() {

        List<Evento> eventos = new ArrayList();

        eventos = eventoRepositorio.findAll();

        return eventos;
    }
    
    public Evento getOne(String id){
       return eventoRepositorio.getOne(id);
    }

    @Transactional
    public void eliminarEvento(String id) {
        Optional<Evento> respuestaEvento = eventoRepositorio.findById(id);

        if (respuestaEvento.isPresent()) {
            eventoRepositorio.deleteById(id);
        }

    }

    private void validar(String id, String nombre, Date fecha, String link, String idProyecto) throws MiException {
        if (id == null) {
            throw new MiException("Id no puede ser nulo");
        }
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("nombre no puede ser nulo");
        }
        if (fecha == null) {
            throw new MiException("fecha no puede ser nula");
        }
        if (link.isEmpty() || link == null) {
            throw new MiException("link no puede ser nulo");
        }
        if (idProyecto.isEmpty() || idProyecto == null) {
            throw new MiException("Id proyecto es nulo");
        }
    }

}
