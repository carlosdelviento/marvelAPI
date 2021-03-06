package uy.com.cb.web;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;
import uy.com.cb.entities.CharacterResponse;
import uy.com.cb.service.MarvelService;

@Controller
@Slf4j
public class MarvelController {

	@Autowired
	private MarvelService marvelService;

	@GetMapping("/")
		public String inicio(@ModelAttribute CharacterResponse personaje, Model model, @AuthenticationPrincipal User user) throws Exception {
			try {
				var personajes = marvelService.listarPersonajes().getData().getResults();
				//var comics = marvelService.listarPersonajes().getData().getResults().get(0).getComics().getItems();
				log.info("Ejecutando el controlador Spring MVC");
				model.addAttribute("personajes", personajes);
				//model.addAttribute("comics", comics);
				model.addAttribute("totalPersonajes", personajes.size());
				//return findPaginated(1, "nombre", "asc", model);
				return "index";				
			} catch (Exception e) {
				model.addAttribute("error", e.getMessage());
				return "error";
			}
	}
	
	@GetMapping("/marvel/characters/{id}")
	public String detallePersonaje(Model model, @PathVariable("id") Long id) throws Exception {
		try {
			CharacterResponse personaje = marvelService.encontrarPersonaje(id);
			model.addAttribute("personaje", personaje);
			return "layout/detalle";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}
	
	@GetMapping("/marvel/characters")
	public String busquedaPersonaje(Model model, @RequestParam(value = "id", required = false) Long id) throws Exception {
		try {
			CharacterResponse personajes = marvelService.encontrarPersonaje(id);
			model.addAttribute("personajes", personajes);
			return "layout/busqueda";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}
	
	/*
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		
		Page<CharacterResponse> page = marvelService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<CharacterResponse> listPersonajes = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		//Contador de personajes en la vista de paginacion
		var personajes = marvelService.listarPersonajes().getData().getResults();
		
		model.addAttribute("totalPersonajes", personajes.size());
		
		//Modelo de personas con paginacion maximo 5
		model.addAttribute("listPersonajes", listPersonajes);
		return "index";
	}*/
	
	/**
	 * 
	 * Rest Services
	 */
	@ResponseStatus(OK)
	@ResponseBody
	//@GetMapping("/marvel/characters")
	public CharacterResponse getPersonajes() throws Exception {
		try {
			return marvelService.listarPersonajes();			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@ResponseStatus(OK)
	@ResponseBody
	//@GetMapping("/marvel/characters/{id}")
	public CharacterResponse getPersonaje(@PathVariable("id") Long id) throws Exception {
		try {
			return marvelService.encontrarPersonaje(id);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
