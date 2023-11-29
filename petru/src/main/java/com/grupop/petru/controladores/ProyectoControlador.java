
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
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
@RequestMapping("/proyecto") //localhost:8080/proyecto
public class ProyectoControlador {
    
    @Autowired
    private ProyectoServicio proyectoServicio;
    @Autowired
    private TareaServicio tareaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
     
    @GetMapping("/{id}")
    public String proyecto(@PathVariable String id, @RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        Proyecto proyecto = proyectoServicio.getOne(id);

        List<Tarea> tareas = tareaServicio.listarTareasProyecto(id);

        modelo.addAttribute("proyecto", proyecto);
        modelo.addAttribute("tareas", tareas);

        return "proyecto.html";
    }
    
    @GetMapping("/registrar")
    public String carga_proyecto(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        List<Usuario> clientes = usuarioServicio.listarClientes();
        List<Usuario> agentes = usuarioServicio.listarColaboradores();
        List<String> visibilidad = new ArrayList();
        for(Visibilidad v: Visibilidad.values()){
            visibilidad.add(v.toString());
        }
        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("agentes", agentes);
        modelo.addAttribute("visibilidad", visibilidad);
        modelo.addAttribute("usuariosession", logueado);

        return "carga_proyectos.html";
    }

    @PostMapping("/registro")
    public String proyectoRegistro(@RequestParam String nombre, @RequestParam(required = false) MultipartFile archivo, 
            @RequestParam String idCliente, @RequestParam String idAgente, ModelMap modelo,
            HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        try {
            proyectoServicio.guardar(archivo, nombre, Visibilidad.PUBLICO, "s",
                    idCliente, idAgente);
        } catch (MiException e) {
            modelo.put("error", e);

            return "inicio.html";
        }

        return "redirect:/inicio";
    }
}
