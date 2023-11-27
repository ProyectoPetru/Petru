

package com.grupop.petru.controladores;



/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Usuario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tarea") //localhost:8080/tarea
public class TareaControlador {
    
    @GetMapping("/registrar")
    public String carga_tareas(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        return "carga_tareas.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificarTarea(@PathVariable String id, @RequestParam String tipoTarea, ModelMap modelo, HttpServletRequest request) {
        modelo.put("error", "La tarea con la id " + id + " esta en " + tipoTarea);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
    
}
