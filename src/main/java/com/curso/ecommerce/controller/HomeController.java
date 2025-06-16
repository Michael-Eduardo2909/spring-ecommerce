package com.curso.ecommerce.controller;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	
	private final Logger log= LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService; 
	
	//para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	// datos de la orden
	Orden orden = new Orden();
	
	@Autowired
	private IUsuarioService usuarioService;
	
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	///----------------------------------------------------------
	
	
	@GetMapping("")
	public String home(Model model) {
		
		model.addAttribute("productos", productoService.findAll());
		
		return "usuario/home";
	}
	 
	
	//NUEVO METODO
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
	    log.info("Id producto enviado como parámetro {}", id);
	    Producto producto = new Producto();
	    Optional<Producto> productoOptional = productoService.get(id);
	    producto = productoOptional.get();
	    
	    
	    model.addAttribute("producto", producto);
	   

	    return "usuario/productohome";
	}
	
	

	
	// AÑADIR PRODUCTOS AL CARRITO DE COMPRAS
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, 
														Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0; //suma todos los totales de los productos añadidos
		
		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido: {}", optionalProducto.get());
		log.info("Cantidad : {}", cantidad);
		producto = optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);
		
		
		//VALIDAR QUE EL MISMO PRODUCTO NO SE AÑADA 2 VECES
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		
		if(!ingresado) {
			detalles.add(detalleOrden); //añadiendo cada detalleorden a la lista
		}
		
		 
		//sumar todos los totales de los productos q esten en esa lista
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal); //SUMA DE TODA LA LISTA DEL CARRITO
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	
	//ELIMINAR PRODUCTO DEL CARRITO
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		
		//lista nueva de productos
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		
		
		for(DetalleOrden detalleOrden: detalles) {
			if(detalleOrden.getProducto().getId() != id) {
				ordenesNueva.add(detalleOrden);
			}
			
		}
		//poner la nueva lista con los productos restantes
		detalles = ordenesNueva;
		
		double sumaTotal = 0;
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal); //SUMA DE TODA LA LISTA DEL CARRITO
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		
		return "usuario/carrito";
	}
	
	
	//VER CARRITO DESDE HOME
	@GetMapping("/getCart")
	public String getCart(Model model) {
		
		model.addAttribute("cart", detalles);                            
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}
	
	
	
	
	// VERORDEN
	@GetMapping("/order")
	public String order(Model model ) {
		
		
		Usuario usuario = usuarioService.findById(1).get();
		
		model.addAttribute("cart", detalles);                            
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		
		return "usuario/resumenorden";
	}
	
	
	
	// GUARDAR LA ORDEN
	@GetMapping("/saveOrder")
	public String saveOrder() {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//usuario
		Usuario usuario = usuarioService.findById(1).get();
		
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		//guardar detalles
		for(DetalleOrden dt:detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		//limpiar lista y orden
		orden = new Orden();
		detalles.clear();
		
		
		return "redirect:/";
	}
	

}
