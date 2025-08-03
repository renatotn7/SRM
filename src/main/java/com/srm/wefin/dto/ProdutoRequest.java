package com.srm.wefin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor // Adicionado para boa prática
@AllArgsConstructor // Adicionado para boa prática
@Schema(description = "Dados para criar ou atualizar um produto.")
public class ProdutoRequest {

	@Schema(description = "Nome do produto.", example = "Espada Mágica de Gelo")
	@NotBlank(message = "O nome do produto não pode estar em branco.")
	@Size(min = 3, max = 100, message = "O nome do produto deve ter entre 3 e 255 caracteres.")
	private String nome;

	@Schema(description = "ID do reino ao qual o produto pertence.", example = "1")
	@NotNull(message = "O ID do reino não pode ser nulo.")
	@Positive(message = "O ID do reino deve ser um número positivo.")
	private Long reinoId;
}