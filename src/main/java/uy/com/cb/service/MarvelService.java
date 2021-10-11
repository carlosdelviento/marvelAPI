package uy.com.cb.service;

import uy.com.cb.controller.response.CharacterResponse;

public interface MarvelService {

	public CharacterResponse listarPersonajes();

	public CharacterResponse encontrarPersonaje(Long id);
	
	//public Page<CharacterResponse> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
