package uy.com.cb.service;

import uy.com.cb.controller.response.CharacterResponse;

public interface MarvelService {

	public CharacterResponse listarPersonajes() throws Exception;

	public CharacterResponse encontrarPersonaje(Long id) throws Exception;
	
	//public Page<CharacterResponse> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
