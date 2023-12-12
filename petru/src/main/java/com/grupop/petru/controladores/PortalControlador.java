
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

import javax.servlet.http.HttpServletRequest;
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

    // a futuro se podria ver si agregamos esto
    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/")
    public String index(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado != null) {
            if (logueado.getRol().toString().equals("CLIENTE") || logueado.getRol().toString().equals("VISITA")
                    || logueado.getRol().toString().equals("COLABORADOR")) {
                return "redirect:/inicio";
            } else if (logueado.getRol().toString().equals("ADMIN")) {
                return "redirect:/admin/dashboard";
            }
        }
        cargarModelo(modelo, session);

        return "inicio.html";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {
        Usuario logueado = cargarModelo(modelo, session);

        modelo.addAttribute("proyectos", proyectoServicio.listarPorUsuario(logueado.getId()));

        return "inicio.html";
    }

    @GetMapping("/registrar")
    public String registrar(HttpSession session, ModelMap modelo) {
        cargarModelo(modelo, session);

        String nombre = (String) session.getAttribute("nombre");
        String email = (String) session.getAttribute("email");
        Long telefono = (Long) session.getAttribute("telefono");
        String descripcion = (String) session.getAttribute("descripcion");
        session.removeAttribute("nombre");
        session.removeAttribute("email");
        session.removeAttribute("telefono");
        session.removeAttribute("descripcion");

        modelo.put("nombre", nombre);
        modelo.put("email", email);
        modelo.put("telefono", telefono);
        modelo.put("descripcion", descripcion);

        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String clave,
            @RequestParam String clave2, @RequestParam MultipartFile archivo,
            @RequestParam(required = false) Long telefono, @RequestParam String descripcion,
            ModelMap modelo, HttpSession session) {
        try {
            usuarioServicio.registrarUsuario(archivo, nombre, email, clave, clave2, telefono, descripcion);

            session.setAttribute("exito", "Usuario registrado correctamente!");

            return "redirect:/";
        } catch (MiException ex) {
            session.setAttribute("error", ex.getMessage());
            session.setAttribute("nombre", nombre);
            session.setAttribute("email", email);
            session.setAttribute("telefono", telefono);
            session.setAttribute("descripcion", descripcion);

            return "redirect:/registrar";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo, HttpSession session) {
        cargarModelo(modelo, session);

        return "login.html";
    }

    @GetMapping("/carga_tareas")
    public String carga_tareas(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        cargarModelo(modelo, session);

        return "carga_tareas.html";
    }

    @GetMapping("/carga_proyecto")
    public String carga_proyecto(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        cargarModelo(modelo, session);

        return "carga_proyectos.html";
    }

    @GetMapping("/contacto")
    public String contacto(@RequestParam(required = false) String error, HttpSession session, ModelMap modelo) {
        cargarModelo(modelo, session);

        return "contacto.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/perfil/{id}")
    public String perfilID(ModelMap modelo, @PathVariable String id) {

        modelo.put("usuario", usuarioServicio.getOne(id));

        return "perfil2.html";
    }

    @PostMapping("/contacto")
    public String contactar(@RequestParam String nombre, @RequestParam String email, @RequestParam String titulo,
            @RequestParam String cuerpo, @RequestParam Long numero, HttpSession session) {
        cuerpo = "<div><h1 style='margin: 0 0 1rem 0'>" + nombre + "</h1>\n<h2 style='margin: 0 0 1rem 0'>" + email
                + "</h2>\n<h2 style='margin: 0 0 1rem 0'>" + numero
                + "</h2>\n<h4 style='margin: 0 1rem 0 1rem; font-weight: normal'>" + cuerpo + "</h4><div>";

        emailServicio.sendEmail(titulo, cuerpo);

        session.setAttribute("exito", "Email enviado con exito");

        return "redirect:/contacto";
    }

    @GetMapping("/usuario/autenticar/{id}")
    public String autenticar(@PathVariable String id, ModelMap modelo, HttpSession session) {
        try {
            Usuario usuario = usuarioServicio.getByToken(id);

            usuarioServicio.modificarRolUsuario(usuario.getId(), Rol.CLIENTE);
            usuarioServicio.inhabilitarToken(id);

            session.setAttribute("exito", "Usuario autenticado con exito!");
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());

            return "redirect:/";
        }

        return "redirect:/login";
    }

    @GetMapping("/mensaje")
    public String getMethodName(@RequestParam String cuerpo,
            @RequestParam String destinatario, HttpSession session, ModelMap modelo, HttpServletRequest request) {
        Usuario usuario = cargarModelo(modelo, session);

        if (usuario == null) {
            session.setAttribute("error", "Tiene que estar logueado para mandar mensajes");

            return "redirect:/";
        }

        String titulo = usuario.getNombre() + " te mando un mensaje";

        cuerpo = "<h4 style='margin: 0 1rem 0 1rem; font-weight: normal'>" + cuerpo + "</h4><div>";

        emailServicio.sendEmail(destinatario, titulo, cuerpo);

        session.setAttribute("exito", "Email enviado con exito");

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/cambiar-clave/{email}")
    public String enviarCambio(ModelMap modelo, HttpSession session, HttpServletRequest request,
            @PathVariable String email) {
        Usuario logueado = cargarModelo(modelo, session);

        if (logueado != null) {
            session.setAttribute("error", "Estas logueado!");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        Usuario usuario = usuarioServicio.getByEmail(email);

        if (usuario != null) {
            Token token = usuarioServicio.asignarToken(usuario);

            String titulo = "Solicitud de modificacion de clave";
            String cuerpo = "<h4 style='margin: 0 1rem 0 1rem; font-weight: normal'>" +
                    "Si ha solicitado la recuperacion de su clave, por favor clickee el siguiente " +
                    "<a href='http://localhost:8080/perfil/cambiar-clave/" + token.getId()
                    + "'>link</a>.<br>Si no ha solicitado esta modificacion ignore el mensaje.</h4>";

            emailServicio.sendEmail(usuario.getEmail(), titulo, cuerpo);

            session.setAttribute("exito", "Se ha enviado un email para verificar que es usted.");
        } else {
            session.setAttribute("error", "Email no encontrado");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/perfil/cambiar-clave/{idToken}")
    public String cambiarCla(ModelMap modelo, HttpSession session, HttpServletRequest request,
            @PathVariable String idToken) {
        Usuario logueado = cargarModelo(modelo, session);

        if (logueado != null) {
            session.setAttribute("error", "Estas logueado!");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        try {
            usuarioServicio.getToken(idToken);
            modelo.put("token", idToken);

            return "cambiar-clave.html";
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/perfil/cambiar-clave/{idToken}")
    public String cambioClave(ModelMap modelo, HttpSession session, HttpServletRequest request,
            @PathVariable String idToken, @RequestParam String clave, @RequestParam String clave2, @RequestParam MultipartFile archivo) {
        Usuario logueado = cargarModelo(modelo, session);

        if (logueado != null) {
            session.setAttribute("error", "Estas logueado!");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        try {
            usuarioServicio.getToken(idToken);
            Usuario usuario = usuarioServicio.getByToken(idToken);
            usuarioServicio.inhabilitarToken(idToken);

            usuarioServicio.modificarUsuario(archivo, usuario.getId(), usuario.getNombre(), usuario.getEmail(), clave, clave2, usuario.getTelefono(), usuario.getDescripcion());

            session.setAttribute("exito", "Clave modificada con exito!");

            return "redirect:/";
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());

            return "redirect:/";
        }
    }

    @GetMapping("/enviar-validacion")
    public String enviarValidacion(ModelMap modelo, HttpSession session, HttpServletRequest request) {
        Usuario usuario = cargarModelo(modelo, session);

        if (usuario == null) {
            session.setAttribute("error", "No estas logueado!");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        Token token = usuarioServicio.asignarToken(usuario);

        String titulo = "Solicitud de modificacion de usuario";
        String cuerpo = "<h4 style='margin: 0 1rem 0 1rem; font-weight: normal'>" +
                "Si has solicitado una modificacion de sus datos, por favor clickee el siguiente " +
                "<a href='http://localhost:8080/perfil/modificar/" + token.getId()
                + "'>link</a>.<br>Si no ha solicitado esta modificacion ignore el mensaje.</h4>";

        emailServicio.sendEmail(usuario.getEmail(), titulo, cuerpo);

        session.setAttribute("exito", "Se ha enviado un email para verificar que es usted.");

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITA', 'ROLE_CLIENTE', 'ROLE_COLABORADOR', 'ROLE_ADMIN')")
    @PostMapping("/perfil/modificar")
    public String actualizar(@RequestParam(required = false) MultipartFile archivo, @RequestParam String nombre,
            @RequestParam Long telefono, @RequestParam String descripcion, HttpSession session, ModelMap modelo,
            HttpServletRequest request)
            throws MiException {
        Usuario usuario = cargarModelo(modelo, session);

        if (usuario == null) {
            throw new MiException("Tienes que estar logueado");
        }

        try {

            session.setAttribute("usuariosession",
                    usuarioServicio.modificarUsuario(usuario.getId(), archivo, nombre, telefono, descripcion));

            session.setAttribute("exito", "Tu usuario se actualizo correctamente!");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        } catch (MiException ex) {

            session.setAttribute("error", ex.getMessage());

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITA', 'ROLE_CLIENTE', 'ROLE_COLABORADOR', 'ROLE_ADMIN')")
    @GetMapping("/perfil/modificar/{idToken}")
    public String validar(ModelMap modelo, HttpSession session, HttpServletRequest request,
            @PathVariable String idToken) {
        Usuario usuario = cargarModelo(modelo, session);

        if (usuario == null) {
            session.setAttribute("error", "No estas logueado!");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        try {
            usuarioServicio.getToken(idToken);
            modelo.put("token", idToken);

            return "perfil.html";
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());
        }

        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITA', 'ROLE_CLIENTE', 'ROLE_COLABORADOR', 'ROLE_ADMIN')")
    @PostMapping("/perfil/modificar/{idToken}")
    public String modificar(ModelMap modelo, HttpSession session, HttpServletRequest request,
            @PathVariable String idToken, @RequestParam(required = false) MultipartFile archivo,
            @RequestParam String nombre, @RequestParam Long telefono, @RequestParam String email,
            @RequestParam String clave, @RequestParam String clave2, @RequestParam String descripcion) {
        Usuario usuario = cargarModelo(modelo, session);

        if (usuario == null) {
            session.setAttribute("error", "No estas logueado!");

            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        try {
            usuarioServicio.getToken(idToken);
            usuarioServicio.inhabilitarToken(idToken);

            session.setAttribute("usuariosession",
                    usuarioServicio.modificarUsuario(archivo, usuario.getId(), nombre, email, clave, clave2, telefono,
                            descripcion));

            session.setAttribute("exito", "Usuario actualizado con exito");

            return "redirect:/";
        } catch (MiException e) {
            session.setAttribute("error", e.getMessage());

            return "redirect:/";
        }
    }
}