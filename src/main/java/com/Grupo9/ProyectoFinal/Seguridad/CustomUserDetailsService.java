package com.Grupo9.ProyectoFinal.Seguridad;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Entidad.Usuario;
import com.Grupo9.ProyectoFinal.Repositorio.EmpleadorRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.TrabajadorRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.UsuarioRepositorio;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    EmpleadorRepositorio empleadorRepositorio;
    @Autowired
    TrabajadorRepositorio trabajadorRepositorio;
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    PasswordEncoder passEncoder;

    public void crearTrabajador(Trabajador trabajador) {
        /*if (trabajadorRepositorio.findByEmail(trabajador.getEmail()) != null) {
            throw new IllegalArgumentException("El usuario ya existe");
        }*/
        trabajador.setContrasena(passEncoder.encode(trabajador.getContrasena()));
        usuarioRepositorio.save(trabajador);
        this.loadUserByUsername(trabajador.getEmail());
    }

    public void crearEmpleador(Empleador empleador) {
        /*if (empleadorRepositorio.buscarPorEmail(empleador.getEmail()) != null) {
            throw new IllegalArgumentException("El usuario ya existe");
        }*/
        empleador.setContrasena(passEncoder.encode(empleador.getContrasena()));
        usuarioRepositorio.save(empleador);
        this.loadUserByUsername(empleador.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            if (empleadorRepositorio.buscarPorEmail(email) == null) {
                throw new UsernameNotFoundException("Este usuario no existe");
            } else {
                Empleador empleador = empleadorRepositorio.buscarPorEmail(email);
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpSession session = attr.getRequest().getSession(true);
                session.setAttribute("usuariosession", empleador);
                List<GrantedAuthority> permisos = new ArrayList<>();
               GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_EMPLEADOR");
               permisos.add(p1);
                return new org.springframework.security.core.userdetails.User(
                        empleador.getEmail(),
                        empleador.getContrasena(),
                        true,
                        true,
                        true,
                        true,
                        permisos)
                        ;
            }

        } catch (UsernameNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            if (trabajadorRepositorio.findByEmail(email) == null) {
                throw new UsernameNotFoundException("Este usuario no existe");
            } else {
                Trabajador trabajador = trabajadorRepositorio.findByEmail(email);
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpSession session = attr.getRequest().getSession(true);
                session.setAttribute("usuariosession", trabajador);
                List<GrantedAuthority> permisos = new ArrayList<>();
                GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_TRABAJADOR");
                permisos.add(p1);
                
                return new org.springframework.security.core.userdetails.User(
                        trabajador.getEmail(),
                        trabajador.getContrasena(),
                        true,
                        true,
                        true,
                        true,
                        permisos);
            }

        } catch (UsernameNotFoundException f) {
            System.out.println(f.getMessage());
//        }
            return null;

        }

    }
}
