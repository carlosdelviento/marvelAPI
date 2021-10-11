package uy.com.cb.web;

import static org.springframework.http.HttpStatus.OK;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;
import uy.com.cb.controller.response.CharacterResponse;
import uy.com.cb.service.MarvelService;

@Controller
@Slf4j
public class MarvelController {

	@Autowired
	private MarvelService marvelService;

	@GetMapping("/")
		public String inicio(@ModelAttribute CharacterResponse personaje, Model model) {
			var personajes = marvelService.listarPersonajes().getData().getResults();
			log.info("Ejecutando el controlador Spring MVC");
			model.addAttribute("listPersonajes", personajes);
			model.addAttribute("totalPersonajes", personajes.size());
			//return findPaginated(1, "nombre", "asc", model);
			return "index";
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
	@GetMapping("/webservices/characters")
	public CharacterResponse getPersonajes() {

		return marvelService.listarPersonajes();
	}

	@ResponseStatus(OK)
	@ResponseBody
	@GetMapping("/webservices/characters/{id}")
	public CharacterResponse getPersonaje(@PathVariable(required = true) @NotNull @Size(min = 7, max = 7) Long id) {

		return marvelService.encontrarPersonaje(id);
	}
}
