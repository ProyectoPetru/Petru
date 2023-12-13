package com.grupop.petru.eventListeners;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.grupop.petru.entidades.Usuario;
import com.grupop.petru.repositorios.UsuarioRepositorio;

@Component
public class AuthenticationEvents {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        String name = userDetails.getUsername();

        Usuario usuario = usuarioRepositorio.buscarPorEmail(name);
        session.setAttribute("usuariosession", usuario);
        session.setAttribute("exito", "Se inicio sesion con exito!");
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("error", "Clave o usuario incorrectos!");

    }
}
