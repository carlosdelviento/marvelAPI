package uy.com.cb.controller.response;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterResponse {

	@NotEmpty
	private String copyright,attributionHTML;

	private DataResponse data;
}
