package uy.com.cb.marvel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import uy.com.cb.controller.response.CharacterResponse;

@FeignClient(name = "webservices", url = "${url.webservices}/v1/public")
public interface MarvelComicsApi {

	@GetMapping("/characters")
	public CharacterResponse listarPersonajes(@RequestParam(value = "ts") Long timeStamp,
			@RequestParam(value = "apikey") String publicKey, @RequestParam(value = "hash") String hashMD5);

	@GetMapping("/characters/{id}")
	public CharacterResponse encontrarPersonaje(
			@PathVariable(required = true) @NotNull @Size(min = 7, max = 7) Long id,
			@RequestParam(value = "ts") Long timeStamp, @RequestParam(value = "apikey") String publicKey,
			@RequestParam(value = "hash") String hashMD5);

}