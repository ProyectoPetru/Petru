package com.grupop.petru.servicios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */


import com.grupop.petru.entidades.Archivo;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.ArchivoRepositorio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoServicio {

    @Autowired
    private ArchivoRepositorio archivoRepositorio;

    @Transactional
    public Archivo guardar(MultipartFile archivo) throws MiException {
        if (!archivo.getContentType().equals("application/octet-stream")) {
            try {
                Archivo aux = new Archivo();
                aux.setTipo(archivo.getContentType());
                aux.setNombre(archivo.getName());
                aux.setContenido(archivo.getBytes());
                return archivoRepositorio.save(aux);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return null;
    }

    @Transactional
    public Archivo actualizar(MultipartFile archivo, String id) throws MiException {
        if (archivo != null) {
            try {
                Archivo aux = new Archivo();
                if (id != null) {
                    Optional<Archivo> respuesta = archivoRepositorio.findById(id);
                    if (respuesta.isPresent()) {
                        aux = respuesta.get();
                    }
                }
                // lo pongo fuera del if, para que en caso que la imágen no exista, se cree una
                // nueva y la devuelva
                // (para evitar que se cargen nulas en la actualizacion de usuario y proyecto)
                aux.setTipo(archivo.getContentType());
                aux.setNombre(archivo.getName());
                aux.setContenido(archivo.getBytes());
                return archivoRepositorio.save(aux);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public void eliminar(String id) throws MiException {
        try {
            Archivo aux = new Archivo();
            if (id != null) {
                Optional<Archivo> respuesta = archivoRepositorio.findById(id);
                if (respuesta.isPresent()) {
                    aux = respuesta.get();
                }
                archivoRepositorio.delete(aux);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Archivo getOne(String id) {
        return archivoRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public Archivo getById(String id) throws MiException {
        Optional<Archivo> aux = archivoRepositorio.findById(id);
        if (aux.isPresent()) {
            return aux.get();
        }
        
        throw new MiException("Archivo no encontrada");
    }

    @Transactional(readOnly = true)
    public List<Archivo> listarTodos() {
        return archivoRepositorio.findAll();
    }
}