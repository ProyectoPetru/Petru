
package com.grupop.petru.controladores;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Comentario;
import com.grupop.petru.entidades.Etiqueta;
import com.grupop.petru.entidades.Proyecto;
import com.grupop.petru.entidades.Tarea;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.TipoTarea;
import com.grupop.petru.enumeraciones.Visibilidad;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.ProyectoServicio;
import com.grupop.petru.servicios.TareaServicio;
import com.grupop.petru.servicios.UsuarioServicio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/proyecto") // localhost:8080/proyecto
public class ProyectoControlador {

    @Autowired
    private ProyectoServicio proyectoServicio;
    @Autowired
    private TareaServicio tareaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

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

    @GetMapping("")
    public String proyectos(@RequestParam(required = false) String error, HttpSession session,
            ModelMap modelo) {
        cargarModelo(modelo, session);

        List<Proyecto> proyectos = proyectoServicio.listarTodos();

        modelo.addAttribute("proyectos", proyectos);

        return "proyecto/listar.html";
    }

    @GetMapping("/unirse/{id}")
    public String unirse(@PathVariable String id, @RequestParam(required = false) String error, HttpSession session,
            ModelMap modelo) {
        Usuario usuario = cargarModelo(modelo, session);

        try {        
            proyectoServicio.invitar(id, usuario.getEmail());

            session.setAttribute("exito", "Se ha unido con exito!");

            return "redirect:/proyecto/" + id;
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }

        return "redirect:/proyecto";
    }

    @PostMapping("/buscar")
    public String buscarUsuario(@RequestParam String busca, @RequestParam String filtro, ModelMap modelo,
            HttpSession session) {
        cargarModelo(modelo, session);

        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        if (filtro.equals("nombre")) {
            proyectos = proyectoServicio.listarPorNombre(busca);
        } else if (filtro.equals("notas")) {
            proyectos = (proyectoServicio.listarPorNotas(busca));
        }
        modelo.addAttribute("proyectos", proyectos);

        return "proyecto/listar.html";
    }

    @PostMapping("/modificar")
    public String modificar(@RequestParam String id, @RequestParam String nombre, @RequestParam String notas, ModelMap modelo,
            HttpSession session) {
        cargarModelo(modelo, session);
        
        try {
            proyectoServicio.modificar(id, nombre, notas);

            session.setAttribute("exito", "Proyecto modificado con exito!");
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }

        return "redirect:/proyecto";
    }

    @GetMapping("/{id}")
    public String proyecto(@PathVariable String id, @RequestParam(required = false) String error, HttpSession session,
            ModelMap modelo) {
        cargarModelo(modelo, session);

        Proyecto proyecto = proyectoServicio.getOne(id);

        List<Tarea> tareas = tareaServicio.listarTareasProyecto(id);

        modelo.addAttribute("proyecto", proyecto);
        modelo.addAttribute("tareas", tareas);

        tareas.forEach(tarea -> tarea.ordernarComentarios());

        return "proyecto.html";
    }

    @PostMapping("/{id}/invitar")
    public String invitar(@PathVariable String id, @RequestParam String email, HttpSession session, ModelMap modelo,
            HttpServletRequest request) {
        try {
            proyectoServicio.invitar(id, email);

            session.setAttribute("exito", "Usuario invitado con exito");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
    }

    @GetMapping("/registrar")
    public String carga_proyecto(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        cargarModelo(modelo, session);
        Usuario logueado = cargarModelo(modelo, session);

        List<Usuario> clientes = usuarioServicio.listarClientes();
        List<Usuario> agentes = usuarioServicio.listarColaboradores();
        List<String> visibilidad = new ArrayList();
        for (Visibilidad v : Visibilidad.values()) {
            visibilidad.add(v.toString());
        }
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("agentes", agentes);
        modelo.addAttribute("visibilidad", visibilidad);
        modelo.addAttribute("proyectos", proyectoServicio.listarPorUsuario(logueado.getId()));

        return "lista_proyectosNuevo.html";
    }

    @PostMapping("/registro")
    public String proyectoRegistro(@RequestParam String nombre, @RequestParam(required = false) MultipartFile archivo,
            @RequestParam(required = false) String idCliente, @RequestParam(required = false) String idAgente,
            String fechaLimite, Visibilidad visibilidad, ModelMap modelo,
            HttpSession session, HttpServletRequest request) throws ParseException {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);
        
        try {
            if (idCliente == null && idAgente == null) {
                proyectoServicio.guardar(archivo, nombre, Visibilidad.GRUPO, "s",
                        Arrays.asList(new Usuario[] { logueado }));
            } else {
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
                Date fecha = formato.parse(fechaLimite);
                proyectoServicio.guardar(archivo, nombre, visibilidad, "s",
                        idCliente, idAgente, fecha);
      //          modelo.put("exito", "El Proyecto se cargó correctamente!");
            }
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
        session.setAttribute("exito", "Proyecto registrado con exito!");

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
