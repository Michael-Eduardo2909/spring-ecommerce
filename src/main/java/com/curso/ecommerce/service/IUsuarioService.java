package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Usuario;

public interface IUsuarioService {

	//DEFINIR LOS METODOS
	Optional<Usuario> findById(Integer id); 
	
	//METODO GUARDAR USUARIO
	Usuario save(Usuario usuario);
	
	//METODO FILTRAR
	Optional<Usuario> findByEmail(String email);
	
	
	//METODO Q PERMITA OBTENER TODOS LOS USUARIOS
	List<Usuario> findAll();
	
	
}
