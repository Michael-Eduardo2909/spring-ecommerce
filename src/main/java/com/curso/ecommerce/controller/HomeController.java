package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	
	private final Logger log= LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService; 
	
	
	
	@GetMapping("")
	public String home(Model model) {
		
		model.addAttribute("productos", productoService.findAll());
		
		return "usuario/home";
	}
	
	
	//NUEVO METODO
//	@GetMapping("productohome/{id}")
//	public String productoHome(@PathVariable Integer id, Model model) {
//	    log.info("Id producto enviado como parámetro {}", id);
//
//	    Optional<Producto> productoOptional = productoService.get(id);
//
//	    if (productoOptional.isPresent()) {
//	        model.addAttribute("producto", productoOptional.get()); // 👈 Pasas solo el Producto, no el Optional
//	    } else {
//	        log.warn("Producto con id {} no encontrado", id);
//	        return "redirect:/"; // 👈 Puedes redirigir o mostrar una vista personalizada de error
//	    }
//
//	    return "usuario/productohome";
//	}
	
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
	    log.info("Id producto enviado como parámetro {}", id);
	    Producto producto = new Producto();
	    Optional<Producto> productoOptional = productoService.get(id);
	    producto = productoOptional.get();
	    
	    
	    model.addAttribute("producto", producto);
	   

	    return "usuario/productohome";
	}

	
	
	

}
