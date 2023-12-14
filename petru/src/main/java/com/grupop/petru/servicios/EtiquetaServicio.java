package com.grupop.petru.servicios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Etiqueta;
import com.grupop.petru.entidades.Tarea;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.EtiquetaRepositorio;
import com.grupop.petru.repositorios.TareaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EtiquetaServicio {
    @Autowired
    private EtiquetaRepositorio etiquetaRepositorio;
    @Autowired
    private TareaRepositorio tareaRepositorio;

    // -------------------------------------CREAR-----------------------------------------------------//
    @Transactional
    public void crearEtiqueta(String idTarea, String nombre, String color) throws MiException {
        Tarea tarea = tareaRepositorio.getOne(idTarea);

        Etiqueta etiqueta = new Etiqueta();

        etiqueta.setNombre(nombre);
        etiqueta.setColor(color);

        etiqueta = etiquetaRepositorio.save(etiqueta);

        tarea.addEtiqueta(etiqueta);

        tareaRepositorio.save(tarea);
    }

    // -----------------------------------------LISTAR
    // -----------------------------------------------------//
    public List<Etiqueta> listarEtiquetas() {

        List<Etiqueta> etiqueta = new ArrayList();

        etiqueta = etiquetaRepositorio.findAll();

        return etiqueta;
    }

    // -------------------------------------MODIFICAR-----------------------------------------------------//
    public void modificarEtiqueta(String id, String nombre, String color) throws MiException {

        validar(id, nombre, color);

        Optional<Etiqueta> respuestaEtiqueta = etiquetaRepositorio.findById(id);

        if (respuestaEtiqueta.isPresent()) {

            Etiqueta etiqueta = respuestaEtiqueta.get();

            etiqueta.setId(id);
            etiqueta.setNombre(nombre);
            etiqueta.setColor(color);

            etiquetaRepositorio.save(etiqueta);
        }
    }

    // -------------------------------------ELIMINAR
    // -----------------------------------------------------//
    public void EliminarEtiqueta(String id) {

        Optional<Etiqueta> respuestaTarea = etiquetaRepositorio.findById(id);

        if (respuestaTarea.isPresent()) {

            etiquetaRepositorio.deleteById(id);
        }
    }

    // -------------------------------------VALIDAR
    // -----------------------------------------------------//
    private void validar(String id, String nombre, String color) throws MiException {

        if (id == null) {
            throw new MiException("El ID no puede ser NULO");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El NOMBRE no puede ser NULO o estar VACIO");
        }

        if (color == null || color.isEmpty()) {
            throw new MiException("El COLOR no puede ser NULO o estar VACIO");
        }
    }
}
