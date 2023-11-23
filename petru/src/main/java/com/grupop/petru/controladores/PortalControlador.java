
package com.grupop.petru.controladores;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/")
public class PortalControlador {
   
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String index() {
        return "inicio.html";
    }
    
    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String clave,
           @RequestParam String clave2, MultipartFile archivo, @RequestParam Long telefono, String descripcion, ModelMap modelo) {
        try {
            usuarioServicio.registrarUsuario(archivo, nombre, email, clave, clave2, telefono, descripcion);
            modelo.put("exito", "Usuario registrado correctamente!");
            return "index.html";
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
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }
        return "login.html";
    }

}
