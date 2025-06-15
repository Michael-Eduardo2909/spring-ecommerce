package com.curso.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	
	//variable 
	private String folder="images//"; //ubicacion que cargara las imagenes

	//METODO
	public String saveImage(MultipartFile file) throws IOException {
		//VALIDACION
		if(!file.isEmpty()) {
			byte [] bytes = file.getBytes();
			Path path = Paths.get(folder + file.getOriginalFilename()); 
			Files.write(path, bytes);
			
			return file.getOriginalFilename(); //retorno nombre de imagen subida
		}
		
		return "default.jpg"; //en caso el usuario no haya asignado una imagen a ese producto, se guardara una imagen por defecto para que se muestre
	}
	
	
	//METODO ELIMINAR IMAGEN
	public void deleteImage(String nombre) {
		String ruta = "";
		File file = new File(ruta + nombre);
		file.delete();
	}
	
	
	
	
}
