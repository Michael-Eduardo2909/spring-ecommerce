package com.curso.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	
	@Autowired
	private IUsuarioService usuarioService;
	
	
	@Autowired
	private IOrdenService ordenService;
	
	///---------------------------------------------------------
	
	
	//METODO MOSTRAR LA PAGINA DE REGISTRO
	//  usuario/registro
	@GetMapping("/registro")
	public String create() {
		
		return "usuario/registro";
	}
	
	
	// GUARDAR EL USUARIO
	@PostMapping("/save")
	public String save(Usuario usuario) {
		logger.info("Usuario registro: {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		
		return "redirect:/";
	}
	
	
	//MOSTRAR LA VISTA LOGIN DE USUARIO
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	
	
	//AUTENTICACION DE USUARIO TEMPORAL
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		logger.info("Accesos : {}", usuario);
		
		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
		//logger.info("Usuario de db: {}", user.get());
		
		//
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if(user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			logger.info("Usuario no existe");
		}
		
		return "redirect:/";
	}
	
	
	
	//MOSTRAR LA VISTA COMPRAS PARA EL USUARIO
	@GetMapping("/compras")
	public String obtenerCompras(Model model ,HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		//utilizando IOrdenService y utilizar el metodo
		Usuario usuario = usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		
		return "usuario/compras";
	}
	
	
	

}
