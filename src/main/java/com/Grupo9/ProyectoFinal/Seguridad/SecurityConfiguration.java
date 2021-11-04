package com.Grupo9.ProyectoFinal.Seguridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author delam
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Configuration
    @Order(1)
    public static class App1ConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        UserDetailsService userDetailsService;

        public App1ConfigurationAdapter() {
            super();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.authorizeRequests()
                    .antMatchers("/registro-trabajador", "/registro-empleador").permitAll()
                    .anyRequest().authenticated();
            http.formLogin()
                    .loginPage("/registro-trabajador")
                    .failureForwardUrl("/login?error")
                    .permitAll();

        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Configuration
        @Order(2)
        public static class App2ConfigurationAdapter extends WebSecurityConfigurerAdapter {

            @Autowired
            UserDetailsService userDetailsService;

            public App2ConfigurationAdapter() {
                super();
            }

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.csrf().disable();
                http.authorizeRequests()
                        .antMatchers("/registro-trabajador", "/registro-empleador").permitAll()
                        .anyRequest().authenticated();
                http.formLogin()
                        .loginPage("/registro-empleador")
                        .failureForwardUrl("/login?error")
                        .permitAll();

            }

            @Autowired
            @Bean
            public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
            }

            public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
            }

        }
    }
    
