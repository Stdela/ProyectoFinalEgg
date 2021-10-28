package com.Grupo9.ProyectoFinal.Entidad;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author delam
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Comentario extends AbstractPersistable<Long> {

    @NotNull
    private String comentario;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer puntaje;
//    @ManyToOne
//    private Usuario usuario;
    
    
    
    

}
