
package com.grupop.petru.servicios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Imagen;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.Rol;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrarUsuario(MultipartFile archivo, String nombre, String email, String clave, String clave2,
            Long telefono, String descripcion) throws MiException {
        validar(nombre, email, clave, clave2, telefono);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setClave(new BCryptPasswordEncoder().encode(clave));
        usuario.setRol(Rol.VISITA);
        usuario.setBaja(Boolean.FALSE);
        usuario.setTelefono(telefono);
        usuario.setDescripcion(descripcion);
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);
    }



    // MODIFICAR USUARIO
    // El rol y la baja se levantan al encontrar el usuario y se dejan igual en este
    // método
    @Transactional
    public void modificarUsuario(MultipartFile archivo, String idUsuario, String nombre, String email, String clave,
            String clave2,
            Long telefono, String descripcion) throws MiException {
        validar(nombre, email, clave, clave2, telefono);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setClave(new BCryptPasswordEncoder().encode(clave));
            Imagen imagen = imagenServicio.guardar(archivo);
            usuario.setImagen(imagen);
            usuario.setTelefono(telefono);
            usuario.setDescripcion(descripcion);
            usuarioRepositorio.save(usuario);
        }
    }



    // ALTA Y BAJA DE USUARIO
    @Transactional
    public void altaDeUsuario(String idUsuario) throws MiException {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(false);
            usuarioRepositorio.save(usuario);
        }

    }

    @Transactional
    public void bajaDeUsuario(String idUsuario) throws MiException {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(true);
            usuarioRepositorio.save(usuario);
        }

    }



    // MODIFICAR ROL

    @Transactional
    public void modificarRolUsuario(String idUsuario, Rol rol) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setRol(rol);
            usuarioRepositorio.save(usuario);
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    // VALIDACIONES

    private void validar(String nombre, String email, String clave, String clave2, Long telefono) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacio");
        }
        if (clave.isEmpty() || clave == null || clave.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!clave.equals(clave2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
        if (telefono == null) {
            throw new MiException("el telefono no puede ser nulo o estar vacio");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getClave(), permisos);
        } else {
            return null;
        }
    }

}
