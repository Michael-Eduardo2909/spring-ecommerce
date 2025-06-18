package com.curso.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer>{

	//METODO Q PERMITA OBTENER TODAS LAS ORDENES POR USUARIO
	List<Orden> findByUsuario(Usuario usuario);
	
}
