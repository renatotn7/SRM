package com.srm.wefin.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ReinoRequest {

	@NotBlank(message = "O nome do reino não pode estar em branco")
	@Size(min = 3, max = 50, message = "O nome do reino deve ter entre 3 e 50 caracteres")
	private String nome;
}