
package com.grupop.petru.servicios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Imagen;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.ImagenRepositorio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepositorio imagenRepositorio;
    @Value("${spring.page.url}")
    private String url;

    @Transactional
    public Imagen guardar(MultipartFile archivo) throws MiException {
        if (!archivo.getContentType().equals("application/octet-stream")) {
            try {
                Imagen imagen = new Imagen();
                imagen.setTipo(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(url + "/img/user.png");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        try {
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Imagen imagen = new Imagen();
        imagen.setNombre("user");
        imagen.setTipo("image/png");
        imagen.setContenido(buffer.toByteArray());
        return imagenRepositorio.save(imagen);
    }

    @Transactional
    public Imagen actualizar(MultipartFile archivo, String id) throws MiException {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                if (id != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(id);
                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }
                }
                // lo pongo fuera del if, para que en caso que la imágen no exista, se cree una
                // nueva y la devuelva
                // (para evitar que se cargen nulas en la actualizacion de usuario y proyecto)
                imagen.setTipo(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public void eliminar(String id) throws MiException {
        try {
            Imagen imagen = new Imagen();
            if (id != null) {
                Optional<Imagen> respuesta = imagenRepositorio.findById(id);
                if (respuesta.isPresent()) {
                    imagen = respuesta.get();
                }
                imagenRepositorio.delete(imagen);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Imagen getOne(String id) {
        return imagenRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public Imagen getById(String id) throws MiException {
        Optional<Imagen> imagen = imagenRepositorio.findById(id);
        if (imagen.isPresent()) {
            return imagen.get();
        }
        
        throw new MiException("Imagen no encontrada");
    }

    @Transactional(readOnly = true)
    public List<Imagen> listarTodos() {
        return imagenRepositorio.findAll();
    }
}
