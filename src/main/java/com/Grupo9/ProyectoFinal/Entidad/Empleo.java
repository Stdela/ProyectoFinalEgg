package com.Grupo9.ProyectoFinal.Entidad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.Grupo9.ProyectoFinal.Enum.Oficio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE empleo SET borrado = true WHERE id=?")
@Where(clause = "borrado=false")
public class Empleo {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
	@NotNull
	private String nombre;
	@NotNull
	private String descripcion;
	@NotNull
	private Oficio oficio;
	@NotNull
	private LocalDate fechaPublicacion;
	@ManyToOne
	private Empleador empleador;
	@ManyToMany(mappedBy = "postulaciones")
	private List<Trabajador> trabajador;
	private Boolean concretado=false;
	private Boolean borrado=false;
	private Boolean finalizado=false;
	
	
	
	

}
