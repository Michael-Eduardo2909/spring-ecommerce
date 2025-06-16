package com.curso.ecommerce.service;

import java.util.Optional;

import com.curso.ecommerce.model.Usuario;

public interface IUsuarioService {

	//DEFINIR LOS METODOS
	Optional<Usuario> findById(Integer id); 
	
}
