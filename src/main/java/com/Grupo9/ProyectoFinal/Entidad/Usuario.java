package com.Grupo9.ProyectoFinal.Entidad;

import com.Grupo9.ProyectoFinal.Enum.Zona;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@SQLDelete(sql = "UPDATE usuario SET alta = true WHERE id=?")
@Where(clause = "deleted=false")

public class Usuario {

    @Id
    protected Long id;
    @Email
    @NotNull
    protected String email;
    @NotNull
    protected String contrasena;
    @Column(name = "nombre_usuario", nullable = false)
    protected String nombreUsuario;
    protected List<String> rol;
    protected String nombre;
    protected String apellido;
    protected Zona zona;
    protected String telefono;
    protected String presentacion;
    protected List<Comentario> comentarios;
    @Lob
    @Basic
    protected byte[] imagen;
    protected boolean alta = false;

}
