
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
import java.util.Arrays;
import java.util.Date;
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
    
    
    
    @GetMapping("/{id}")
    public String proyecto(@PathVariable String id, @RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario seba = new Usuario();
        seba.setNombre("Sebastian");

        modelo.addAttribute("usuariosession", logueado);

        Proyecto proyecto = proyectoServicio.getOne(id);

        Tarea tarea1 = new Tarea();
        tarea1.setId("tar1");
        tarea1.setNombre("Tarea 1");
        tarea1.setProyecto(proyecto);
        tarea1.setTipoTarea(TipoTarea.TODO);
        Etiqueta etiqueta1 = new Etiqueta();
        Etiqueta etiqueta2 = new Etiqueta();
        Etiqueta etiqueta3 = new Etiqueta();
        etiqueta1.setNombre("Programacion");
        etiqueta1.setColor("blue");
        etiqueta2.setNombre("Otra cosa");
        etiqueta2.setColor("red");
        etiqueta3.setNombre("Matematica");
        etiqueta3.setColor("yellow");
        tarea1.setEtiquetas(Arrays.asList(new Etiqueta[] { etiqueta1, etiqueta2, etiqueta3 }));
        Comentario comentario1 = new Comentario();
        comentario1.setId("com1");
        comentario1.setContenido("hola");
        comentario1.setUsuario(seba);
        comentario1.setFecha(new Date());
        Comentario comentario2 = new Comentario();
        comentario2.setId("com2");
        comentario2.setContenido("Deja de adelantarte manuel!!!");
        comentario2.setUsuario(seba);
        comentario2.setFecha(new Date());
        Comentario comentario3 = new Comentario();
        comentario3.setId("com3");
        comentario3.setContenido(
                "Bueno disculpa, aca tenes un lorem awujodbawuib dauw buidwabuwiab duawbd uiawbuidbwauiauidbuiwa");
        comentario3.setUsuario(seba);
        comentario3.setFecha(new Date());
        Comentario comentario4 = new Comentario();
        comentario4.setId("com4");
        comentario4.setContenido("Tenes que estar re aburrido, no?");
        comentario4.setUsuario(seba);
        comentario4.setFecha(new Date());
        tarea1.setComentarios(Arrays.asList(new Comentario[] { comentario1, comentario2, comentario3, comentario4 }));

        Tarea tarea2 = new Tarea();
        tarea2.setId("tar2");
        tarea2.setNombre("Tarea 2");
        tarea2.setProyecto(proyecto);
        tarea2.setTipoTarea(TipoTarea.DOING);
        Etiqueta etiqueta4 = new Etiqueta();
        etiqueta4.setNombre("Agricultura");
        etiqueta4.setColor("green");
        tarea2.setEtiquetas(Arrays.asList(new Etiqueta[] { etiqueta4 }));

        Tarea tarea3 = new Tarea();
        tarea3.setId("tar3");
        tarea3.setNombre("Tarea 3");
        tarea3.setProyecto(proyecto);
        tarea3.setTipoTarea(TipoTarea.DONE);

        modelo.addAttribute("proyecto", proyecto);
        modelo.addAttribute("tareas", Arrays.asList(new Tarea[] { tarea1, tarea2, tarea3 }));

        return "proyecto.html";
    }
    
    @GetMapping("/registrar")
    public String carga_proyecto(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        return "carga_proyectos.html";
    }

    @PostMapping("/registro")
    public String proyectoRegistro(@RequestParam String nombre, @RequestParam(required = false) MultipartFile archivo, ModelMap modelo,
            HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        try {
            proyectoServicio.guardar(archivo, nombre, Visibilidad.PUBLICO, "s",
                    Arrays.asList(new Usuario[] { logueado }));
        } catch (MiException e) {
            modelo.put("error", e);

            return "inicio.html";
        }

        return "redirect:/inicio";
    }

}
