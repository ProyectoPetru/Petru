
package com.grupop.petru;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.grupop.petru.servicios.UsuarioServicio;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Deprecated
public class SeguridadWeb extends WebSecurityConfigurerAdapter {

    @Autowired
    public UsuarioServicio usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
   @Override
    protected void configure(HttpSecurity http) throws Exception { // Este método se sobrescribe para agregar más                                                                 // configuraciones

        http
                .authorizeRequests(requests -> requests // utilizando sintaxis Lambda DSL para configurar las  solicitudes HTTP.                                                        
                        .antMatchers("/admin/*").hasRole("ADMIN") // para preautorizar a los roles del controlador ADMIN                                                      
                        .antMatchers("/admin").hasRole("ADMIN") // para preautorizar a los roles del controlador ADMIN                                                   
                        .antMatchers("/admin/**").hasRole("ADMIN") // para preautorizar a los roles del controlador ADMIN
                        .antMatchers("/css/*", "/js/*", "/img/*", "/**") //// autorizando todas las solicitudes a los patrones de URL especificados                                                                       
                        .permitAll())

                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("email")
                        .passwordParameter("clave")
                        .defaultSuccessUrl("/")
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll())

                .csrf(csrf -> csrf
                   .disable());

               

    }

}