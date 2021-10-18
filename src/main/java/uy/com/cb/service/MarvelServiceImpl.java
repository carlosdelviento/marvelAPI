package uy.com.cb.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uy.com.cb.entities.*;
import uy.com.cb.marvel.MarvelComicsApi;

@Service
public class MarvelServiceImpl implements MarvelService{
	private static final String PUBLIC_KEY = "5c85ed4a749f1f858b4d9bf28c5d8c33";
	private static final String PRIVATE_KEY = "2f595dbc965d28774e4080bf0d5ef01783332dfd";

	@Autowired
	private MarvelComicsApi client;

	// Busqueda de todos los personajes y retorna json por URI
	@Override
	@Transactional(readOnly = true)
	@Cacheable("personajes")
	public CharacterResponse listarPersonajes() throws Exception {
		
		try {
			Long timeStamp = new Date().getTime();			
			return client.listarPersonajes(timeStamp, PUBLIC_KEY, buildHash(timeStamp));
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	// Busqueda por id del personaje y retornar json por URI
	@Override
	@Transactional(readOnly = true)
	@Caching(evict = {
			@CacheEvict(value="personaje", key="#p0"),
			@CacheEvict(value="personajes", allEntries=true)})
	public CharacterResponse encontrarPersonaje(Long id) throws Exception {
		
		try {
			Long timeStamp = new Date().getTime();
			return client.encontrarPersonaje(id, timeStamp, PUBLIC_KEY, buildHash(timeStamp));			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	// Generar hash md5
	private String buildHash(Long timeStamp) {
		return DigestUtils.md5Hex(timeStamp + PRIVATE_KEY + PUBLIC_KEY);
	}

	/*@Override
	public Page<CharacterResponse> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.client.listarPersonajes(pageable);
	}*/
}