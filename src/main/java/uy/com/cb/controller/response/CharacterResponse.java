package uy.com.cb.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterResponse {
	
	private String copyright,attributionHTML;

	private DataResponse data;
}
