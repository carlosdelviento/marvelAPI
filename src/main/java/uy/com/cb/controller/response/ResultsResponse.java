package uy.com.cb.controller.response;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private String name, description;
	
	private ComicResponse comics;
	

}