package com.srm.wefin.dto;

import lombok.AllArgsConstructor; // Importe esta anotação
import lombok.Data;
import lombok.NoArgsConstructor; // Importe esta anotação

import io.swagger.v3.oas.annotations.media.Schema; // Importe esta anotação

import jakarta.validation.constraints.NotBlank; // Importe esta anotação
import jakarta.validation.constraints.Size; // Importe esta anotação

@Data
@NoArgsConstructor // Adicionado para boa prática (necessário para serialização/desserialização)
@AllArgsConstructor // Adicionado para boa prática (construtor com todos os argumentos)
@Schema(description = "Dados para criar ou atualizar uma moeda.") // Documentação geral do DTO
public class MoedaRequest {

	@Schema(description = "Nome completo da moeda (ex: 'Ouro', 'Prata', 'Crédito Estelar').", example = "Ouro")
	@NotBlank(message = "O nome da moeda não pode estar em branco.") // Valida que não é nulo e não está vazio após trim
	@Size(min = 2, max = 100, message = "O nome da moeda deve ter entre 2 e 100 caracteres.") // Limita o tamanho
	private String nome;

	@Schema(description = "Símbolo curto da moeda (ex: 'O', 'Pr', '$').", example = "O")
	@NotBlank(message = "O símbolo da moeda não pode estar em branco.") // Valida que não é nulo e não está vazio após trim
	@Size(min = 1, max = 10, message = "O símbolo da moeda deve ter entre 1 e 10 caracteres.") // Limita o tamanho
	private String simbolo;
}