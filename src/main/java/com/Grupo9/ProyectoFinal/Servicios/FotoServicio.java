package com.Grupo9.ProyectoFinal.Servicios;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Grupo9.ProyectoFinal.Entidad.Foto;
import com.Grupo9.ProyectoFinal.Repositorio.FotoRepositorio;

@Service
public class FotoServicio {
	
	@Autowired
	FotoRepositorio fr;
	
	public Foto guardar(MultipartFile archivo) throws IOException {
		if (archivo != null) {
			try {
				Foto foto = new Foto();
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getOriginalFilename());
				foto.setContenido(archivo.getBytes());
			
			return fr.save(foto);
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return null;
		
	}
	
	public Foto modificar(String idFoto, MultipartFile archivo) throws IOException {
		if (archivo != null) {
			try {
				Foto foto = new Foto();
				
				if (idFoto != null) {
					Optional<Foto> resp = fr.findById(idFoto);
					if (resp.isPresent()) {
						foto = resp.get();
					}		
				}
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getOriginalFilename());
				foto.setContenido(archivo.getBytes());
			
			return fr.save(foto);
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return null;
		
	}
}
