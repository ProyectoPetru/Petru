
package com.grupop.petru.servicios;

/**
 *
 * @authors  Nahiara Denice Alegre - Matias Quispe - Juan Pablo Pontini
 *           Flavio Romero Averna - Dario Litterio - Cecilia Alsina
 *           Manuel Dominich Martinez - Maximo Carbonetti
 *           Salvador Caldarella - Sebastián A. Petrini
 */

import com.grupop.petru.entidades.Imagen;
import com.grupop.petru.entidades.Token;
import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.enumeraciones.Rol;
import com.grupop.petru.excepciones.MiException;
import com.grupop.petru.repositorios.TokenRepositorio;
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
    private TokenRepositorio tokenRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private EmailServicio emailServicio;

    @Transactional
    public void registrarUsuario(MultipartFile archivo, String nombre, String email, String clave, String clave2,
            Long telefono, String descripcion) throws MiException {

        validar(nombre, email, clave, clave2, telefono);
        validarYaRegistrado(email);

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

        usuario = usuarioRepositorio.save(usuario);

        Token token = new Token(usuario);

        tokenRepositorio.save(token);

        emailServicio.sendEmail(email, "Verificacion de usuario " + nombre, "<div><h1 style='margin: 0 0 1rem 0'>"
                + nombre + "</h1>\n<h2 style='margin: 0 0 1rem 0'>" + email
                + "</h2>\n<h4 style='margin: 0 1rem 0 1rem; font-weight: normal'>Entra a <a href='http://localhost:8080/usuario/autenticar/"
                + token.getId() + "'>este link</a> para auntenticarte</h4><div>");
    }

    // MODIFICAR USUARIO
    // El rol y la baja se levantan al encontrar el usuario y se dejan igual en este
    // método
    @Transactional
    public void modificarUsuario(MultipartFile archivo, String idUsuario, String nombre, String email, String clave,
            String clave2,
            Long telefono, String descripcion) throws MiException {

        validar(nombre, email, clave, clave2, telefono);
        try {

            Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setNombre(nombre);
                usuario.setEmail(email);
                usuario.setClave(new BCryptPasswordEncoder().encode(clave));

                String idImagen = null;

                if (usuario.getImagen() != null) {
                    idImagen = usuario.getImagen().getId();
                }

                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                usuario.setImagen(imagen);
                usuario.setTelefono(telefono);
                usuario.setDescripcion(descripcion);
                usuarioRepositorio.save(usuario);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Transactional
    public Usuario modificarUsuario(String id, MultipartFile archivo, String nombre, Long telefono, String descripcion)
            throws MiException {
        validar(nombre, telefono);
        try {
            Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();

                usuario.setNombre(nombre);

                /*
                 * //le manda el id del usuario, no de la imagen
                 * if (!archivo.getContentType().equals("application/octet-stream")) {
                 * Imagen imagen = imagenServicio.actualizar(archivo,id);
                 * usuario.setImagen(imagen);
                 * }
                 */

                String idImagen = null;

                if (usuario.getImagen() != null) {
                    idImagen = usuario.getImagen().getId();
                }

                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

                usuario.setImagen(imagen);
                usuario.setTelefono(telefono);
                usuario.setDescripcion(descripcion);

                return usuarioRepositorio.save(usuario);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    // ALTA Y BAJA DE USUARIO
    @Transactional
    public void altaDeUsuario(String idUsuario) throws MiException {
        try {
            Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setBaja(false);
                usuarioRepositorio.save(usuario);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    @Transactional
    public void bajaDeUsuario(String idUsuario) throws MiException {
        try {
            Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setBaja(true);
                usuarioRepositorio.save(usuario);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    // MODIFICAR ROL

    @Transactional
    public void modificarRolUsuario(String idUsuario, Rol rol) throws MiException {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setRol(rol);
            usuarioRepositorio.save(usuario);
        } else {
            throw new MiException("Usuario no encontrado");
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarClientes() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.buscarClientes();
        return usuarios;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarColaboradores() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.buscarColaborador();
        return usuarios;
    }

    // VALIDACIONES

    private void validar(String nombre, String email, String clave, String clave2, Long telefono) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (nombre.length() > 14) {
            throw new MiException("el nombre no puede contener mas de 12 caracteres");
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

    private void validar(String nombre, Long telefono) throws MiException {
        if (nombre.length() > 14) {
            throw new MiException("el nombre no puede contener mas de 12 caracteres");
        }
        if (telefono == null) {
            throw new MiException("el numero de telefono no puede ser nulo o estar vacio");
        }
    }

    public void validarYaRegistrado(String email) throws MiException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            throw new MiException("Ya existe un usuario registrado con ese email");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            return new User(usuario.getEmail(), usuario.getClave(), permisos);
        } else {
            return null;
        }
    }

    public Boolean esCliente(String idCliente) {
        Usuario usuario = getOne(idCliente);
        if (!usuario.getRol().toString().equals("CLIENTE") || usuario.getBaja() == true)
            return false;
        return true;
    }

    public Boolean esColaborador(String idUsuario) {
        Usuario usuario = getOne(idUsuario);
        if (!usuario.getRol().toString().equals("COLABORADOR") || usuario.getBaja() == true)
            return false;
        return true;
    }

    @Transactional(readOnly = true)
    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public Usuario getByToken(String id) throws MiException {
        Usuario usuario = tokenRepositorio.getByToken(id);

        if (usuario == null) {
            throw new MiException("Token invalido o no encontrado");
        }

        return usuario;
    }

    @Transactional(readOnly = true)
    public Token getToken(String id) throws MiException {
        Optional<Token> tokenRes = tokenRepositorio.findById(id);

        if (tokenRes.isPresent()) {
            return tokenRes.get();
        }

        throw new MiException("Token invalido o no encontrado");
    }

    public void inhabilitarToken(String id) throws MiException {
        Token token = tokenRepositorio.getOne(id);

        if (token == null) {
            throw new MiException("Token invalido o no encontrado");
        }

        tokenRepositorio.delete(token);
    }

    @Transactional(readOnly = true)
    public Usuario getByEmail(String email) {
        return usuarioRepositorio.buscarPorEmail(email);
    }
}
