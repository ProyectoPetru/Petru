
package com.grupop.petru.controladores;

import com.grupop.petru.entidades.Tarea;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.TipoTarea;
import com.grupop.petru.enumeraciones.Visibilidad;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.ProyectoServicio;
import com.grupop.petru.servicios.TareaServicio;
import com.grupop.petru.servicios.UsuarioServicio;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tarea") // localhost:8080/tarea
public class TareaControlador {
    @Autowired
    private TareaServicio tareaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ProyectoServicio proyectoServicio;

    private Usuario cargarModelo(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        String error = (String) session.getAttribute("error");
        String exito = (String) session.getAttribute("exito");
        session.removeAttribute("error");
        session.removeAttribute("exito");

        modelo.addAttribute("usuariosession", usuario);
        modelo.put("error", error);
        modelo.put("exito", exito);

        return usuario;
    }

    @GetMapping("/registrar")
    public String carga_tareas(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        List<Usuario> agentes = usuarioServicio.listarColaboradores();
        List<String> visibilidad = new ArrayList();
        for (Visibilidad v : Visibilidad.values()) {
            visibilidad.add(v.toString());
        }
        modelo.addAttribute("agentes", agentes);
        modelo.addAttribute("visibilidad", visibilidad);
        modelo.addAttribute("usuariosession", logueado);
        modelo.addAttribute("proyectos", proyectoServicio.listarPorUsuario(logueado.getId()));
        modelo.addAttribute("tareas", tareaServicio.listarPorUsuario(logueado.getId()));

        return "lista_tareasNueva.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String idProyecto, @RequestParam String nombre, ModelMap modelo,
            HttpServletRequest request) {
        try {
            tareaServicio.crearTarea(idProyecto, nombre);
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/modificar-nombre/{id}")
    public String modificarTarea(@PathVariable String id, @RequestParam String nombre, ModelMap modelo,
            HttpServletRequest request) {
        Tarea tarea = tareaServicio.getOne(id);

        tarea.setNombre(nombre);

        try {
            tareaServicio.modificarTarea(tarea.getId(), tarea.getNombre(), tarea.getProyecto().getId(), tarea.getTipoTarea(), tarea.getEtiquetas());
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/modificar-rol/{id}")
    public String modificarRolTarea(@PathVariable String id, @RequestParam String tipoTarea, ModelMap modelo,
            HttpServletRequest request, HttpSession session) {
        Tarea tarea = tareaServicio.getOne(id);

        switch (tipoTarea) {
            case "TODO":
                tarea.setTipoTarea(TipoTarea.TODO);
                break;
            case "DOING":
                tarea.setTipoTarea(TipoTarea.DOING);
                break;
            case "DONE":
                tarea.setTipoTarea(TipoTarea.DONE);
                break;
        }

        try {
            tareaServicio.modificarTarea(tarea.getId(), tarea.getNombre(), tarea.getProyecto().getId(), tarea.getTipoTarea(), tarea.getEtiquetas());
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
