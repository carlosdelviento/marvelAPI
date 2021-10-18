package uy.com.cb.entities;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

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
	@NotEmpty
	private Long id;
	
	private String name, description;
	
	private ComicResponse comics;
	

}