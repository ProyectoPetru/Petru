
package com.grupop.petru.controladores;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupop.petru.entidades.Comentario;
import com.grupop.petru.entidades.Tarea;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.ComentarioServicio;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

@Controller
@RequestMapping("/comentario") // localhost:8080/comentario
public class ComentarioControlador {
    @Autowired
    private ComentarioServicio comentarioServicio;

    @PostMapping("/registro")
    public String registro(@RequestParam String idTarea, @RequestParam String idUsuario, @RequestParam String contenido, ModelMap modelo,
            HttpServletRequest request) {
        try {
            comentarioServicio.crearComentario(idTarea, idUsuario, contenido);
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
