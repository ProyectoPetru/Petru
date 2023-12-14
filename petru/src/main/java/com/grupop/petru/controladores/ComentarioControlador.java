
package com.grupop.petru.controladores;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.grupop.petru.entidades.Comentario;
import com.grupop.petru.entidades.Tarea;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.Rol;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.ComentarioServicio;
import com.grupop.petru.servicios.ProyectoServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/comentario") // localhost:8080/comentario
public class ComentarioControlador {
    @Autowired
    private ComentarioServicio comentarioServicio;
    @Autowired
    private ProyectoServicio proyectoServicio;

    private Usuario cargarModelo(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        String error = (String) session.getAttribute("error");
        String exito = (String) session.getAttribute("exito");
        session.removeAttribute("error");
        session.removeAttribute("exito");

        if (usuario != null) {
            modelo.addAttribute("proyectos", proyectoServicio.listarPorUsuario(usuario.getId()));
        }
        modelo.addAttribute("usuariosession", usuario);
        modelo.put("error", error);
        modelo.put("exito", exito);

        return usuario;
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String idTarea, @RequestParam String idUsuario, @RequestParam String contenido,
            ModelMap modelo,
            HttpServletRequest request, HttpSession session) {
        try {
            comentarioServicio.crearComentario(idTarea, idUsuario, contenido);

            session.setAttribute("exito", "Comentario agregado con exito!");
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, HttpServletRequest request, HttpSession session, ModelMap modelo) {
        Usuario usuario = cargarModelo(modelo, session);

        try {
            Comentario comentario = comentarioServicio.listarPorId(id);

            if (usuario.getId().equals(comentario.getUsuario().getId()) || usuario.getRol() == Rol.ADMIN) {
                comentarioServicio.borrarComentario(id);

                session.setAttribute("exito", "Comentario eliminado con exito!");
            } else {
                session.setAttribute("error", "El mensaje no te pertenece");
            }
        } catch (MiException e) {
            session.setAttribute("error", "Comentario no encontrado");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
