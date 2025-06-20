package com.curso.ecommerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final IUsuarioService usuarioService;
    private final HttpSession session;

    private Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    public UserDetailServiceImpl(IUsuarioService usuarioService, HttpSession session) {
        this.usuarioService = usuarioService;
        this.session = session;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Buscando usuario: {}", username);
        Optional<Usuario> optionalUser = usuarioService.findByEmail(username);

        if (optionalUser.isPresent()) {
            Usuario usuario = optionalUser.get();
            log.info("Usuario encontrado con ID: {}", usuario.getId());

            session.setAttribute("idusuario", usuario.getId());
 
            return User.builder()
                    .username(usuario.getEmail()) // login con email
                    .password(usuario.getPassword()) // ya está encriptada
                    .roles(usuario.getTipo()) // "ADMIN" o "USER"
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
