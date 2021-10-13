package uy.com.cb.controller.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
}