package com.Grupo9.ProyectoFinal.Entidad;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Zona;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author delam
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE usuario SET borrado = true WHERE id=?")
@Where(clause = "borrado=false")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    @Email
    @NotNull
    protected String email;
    @NotNull
    protected String contrasena;
    @Column(name = "nombre_usuario", nullable = false)
    protected String nombreUsuario;
    protected ArrayList<String> rol/* = new ArrayList<>()*/;
    protected String nombre;
    protected String apellido;
    protected Genero genero;
    protected LocalDate fechaNacimiento;
    protected Zona zona;
    protected String telefono;
    protected String presentacion;
    //@OneToMany
    //protected ArrayList<Comentario> comentarios/* = new ArrayList<>()*/;
    @Lob
    @Basic
    protected byte[] imagen;
    protected boolean borrado = false;

}
