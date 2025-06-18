package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;

public interface IOrdenService {
	
	List<Orden> findAll();
	Orden save (Orden orden);
	
	String generarNumeroOrden();
	
	//IMPLEMENTACION DEL METODO, Q PERMITA OBTENER TODAS LAS ORDENES POR USUARIO
	List<Orden> findByUsuario(Usuario usuario);
	
	
	//OBTENER POR ID
	Optional<Orden> findById(Integer id);
	
}
