
package com.grupop.petru.controladores;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.ListasDashboard;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.Rol;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.ProyectoServicio;
import com.grupop.petru.servicios.UsuarioServicio;
import java.util.ArrayList;
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

@Controller
@RequestMapping("/admin")
public class AdminControlador {

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

        if (usuario != null) {
            modelo.addAttribute("proyectos", proyectoServicio.listarPorUsuario(usuario.getId()));
        }
        modelo.addAttribute("usuariosession", usuario);
        modelo.put("error", error);
        modelo.put("exito", exito);

        return usuario;
    }

    @GetMapping("/dashboard")
    public String panelAdmin(HttpSession session, ModelMap modelo) {
        cargarModelo(modelo, session);

        List<ListasDashboard> usuariosProyecto = usuarioServicio.listarUsuariosProyecto();

        modelo.addAttribute("usuariosProyecto", usuariosProyecto);

        return "dashNueva.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo, HttpSession session) {
        cargarModelo(modelo, session);

        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuarios/listar.html";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id, HttpSession session) {
        try {
            usuarioServicio.modificarRolUsuario(id);

            session.setAttribute("exito", "Rol modificado con exito");
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/modificarBaja/{id}")
    public String cambiarBaja(@PathVariable String id, HttpSession session) {
        try {
            usuarioServicio.modificarBajaUsuario(id);
            session.setAttribute("exito", "Estado modificado con exito");
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/buscar")
    public String buscarUsuario(@RequestParam String busca, @RequestParam String filtro, ModelMap modelo,
            HttpSession session) {
        cargarModelo(modelo, session);

        List<Usuario> usuarios = new ArrayList<Usuario>();
        if (filtro.equals("nombre")) {
            usuarios = usuarioServicio.buscarUsuarios(busca);
        } else if (filtro.equals("email")) {
            usuarios = (usuarioServicio.getByEmails(busca));
        } else if (filtro.equals("telefono")) {
            usuarios = usuarioServicio.getByTelefono(Long.parseLong(busca));
        }
        modelo.addAttribute("usuarios", usuarios);

        return "usuarios/listar.html";
    }

}
