package com.Grupo9.ProyectoFinal.Seguridad;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Entidad.Usuario;
import com.Grupo9.ProyectoFinal.Repositorio.EmpleadorRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.TrabajadorRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.UsuarioRepositorio;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    EmpleadorRepositorio empleadorRepositorio;
    @Autowired
    TrabajadorRepositorio trabajadorRepositorio;
    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.encontrarPorEmail(email);

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getContrasena(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}

//        try {
//            if (empleadorRepositorio.buscarPorEmail(email) == null) {
//                throw new UsernameNotFoundException("Este usuario no existe");
//            } else {
//                Empleador empleador = empleadorRepositorio.buscarPorEmail(email);
//                return new org.springframework.security.core.userdetails.User(
//                        empleador.getEmail(),
//                        empleador.getContrasena(),
//                        true,
//                        true,
//                        true,
//                        true,
//                        Arrays.asList(new SimpleGrantedAuthority("USER")));
//            }
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            if (trabajadorRepositorio.buscarPorEmail(email) == null) {
//                throw new UsernameNotFoundException("Este usuario no existe");
//            } else {
//                Trabajador trabajador = trabajadorRepositorio.buscarPorEmail(email);
//                return new org.springframework.security.core.userdetails.User(
//                        trabajador.getEmail(),
//                        trabajador.getContrasena(),
//                        true,
//                        true,
//                        true,
//                        true,
//                        Arrays.asList(new SimpleGrantedAuthority("USER")));
//            }
//
//        } catch (Exception f) {
////            System.out.println(f.getMessage());
////        }
//        return null;
//
//    }

