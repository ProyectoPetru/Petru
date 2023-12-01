
package com.grupop.petru.controladores;

import com.grupop.petru.entidades.Token;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.Rol;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.EmailServicio;
import com.grupop.petru.servicios.ProyectoServicio;
import com.grupop.petru.servicios.UsuarioServicio;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ProyectoServicio proyectoServicio;
    @Autowired
    private EmailServicio emailServicio;

    // a futuro se podria ver si agregamos esto
    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/")
    public String index(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado == null) {
            return "inicio.html";
        }
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        modelo.addAttribute("proyectos", proyectoServicio.listarPorUsuario(logueado.getId()));

        return "inicio.html";
    }

    @GetMapping("/registrar")
    public String registrar(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String clave,
            @RequestParam String clave2, @RequestParam MultipartFile archivo, @RequestParam Long telefono,
            String descripcion,
            ModelMap modelo) {
        try {
            usuarioServicio.registrarUsuario(archivo, nombre, email, clave, clave2, telefono, descripcion);
            modelo.put("exito", "Usuario registrado correctamente!");
            return "inicio.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            modelo.put("telefono", telefono);
            modelo.put("descripcion", descripcion);
            return "registro.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo, HttpSession session) {
        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }

        return "login.html";
    }

    @GetMapping("/carga_tareas")
    public String carga_tareas(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        return "carga_tareas.html";
    }

    @GetMapping("/carga_proyecto")
    public String carga_proyecto(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        return "carga_proyectos.html";
    }

    @GetMapping("/contacto")
    public String contacto(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("usuariosession", logueado);

        return "contacto.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITA','ROLE_CLIENTE', 'ROLE_COLABORADOR', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        modelo.put("usuariosession", usuario);

        return "perfil.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_COLABORADOR', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String clave,
            String clave2, Long telefono, String descripcion, ModelMap modelo) throws MiException {

        try {

            usuarioServicio.modificarUsuario(archivo, idUsuario, nombre, email, clave, clave2, telefono, descripcion);

            modelo.put("exito", "Tu usuario se actualizo correctamente!");

            return "inicio.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            modelo.put("nombre", nombre);

            modelo.put("email", email);

            return "perfil.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITA', 'ROLE_CLIENTE', 'ROLE_COLABORADOR', 'ROLE_ADMIN')")
    @PostMapping("/perfil/modificar")
    public String modificar(@RequestParam(required = false) MultipartFile archivo, @RequestParam String nombre,
            @RequestParam Long telefono, @RequestParam String descripcion, ModelMap modelo, HttpSession session)
            throws MiException {

        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");

            session.setAttribute("usuariosession",
                    usuarioServicio.modificarUsuario(usuario.getId(), archivo, nombre, telefono, descripcion));

            return "redirect:/inicio";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());

            return "perfil.html";
        }
    }

    @PostMapping("/contacto")
    public String contactar(@RequestParam String nombre, @RequestParam String email, @RequestParam String titulo,
            @RequestParam String cuerpo, @RequestParam Long numero) {
        cuerpo = "<div><h1 style='margin: 0 0 1rem 0'>" + nombre + "</h1>\n<h2 style='margin: 0 0 1rem 0'>" + email
                + "</h2>\n<h2 style='margin: 0 0 1rem 0'>" + numero
                + "</h2>\n<h4 style='margin: 0 1rem 0 1rem; font-weight: normal'>" + cuerpo + "</h4><div>";

        emailServicio.sendEmail(titulo, cuerpo);

        return "redirect:/";
    }

    @GetMapping("/usuario/autenticar/{id}")
    public String autenticar(@PathVariable String id, ModelMap modelo) {
        try {
            Token token = usuarioServicio.getToken(id);

            Usuario usuario = usuarioServicio.getByToken(id);

            usuarioServicio.modificarRolUsuario(usuario.getId(), Rol.CLIENTE);
            usuarioServicio.inhabilitarToken(id);
        } catch (MiException e) {
            modelo.put("error", e.getMessage());

            return "inicio.html";
        }

        return "redirect:/login";
    }
}