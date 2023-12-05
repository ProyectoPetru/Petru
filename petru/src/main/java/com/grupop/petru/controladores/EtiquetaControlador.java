
package com.grupop.petru.controladores;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupop.petru.entidades.Tarea;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.EtiquetaServicio;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

@Controller
@RequestMapping("/etiqueta") // localhost:8080/etiqueta
public class EtiquetaControlador {
    @Autowired
    private EtiquetaServicio etiquetaServicio;

    @PostMapping("/registro")
    public String registro(@RequestParam String idTarea, @RequestParam String nombre, @RequestParam String color, ModelMap modelo,
            HttpServletRequest request, HttpSession session) {
        try {
            etiquetaServicio.crearEtiqueta(idTarea, nombre, color);
            
            session.setAttribute("exito", "Etiqueta creada con exito!");
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
