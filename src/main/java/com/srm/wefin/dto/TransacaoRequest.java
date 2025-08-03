package com.srm.wefin.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.PastOrPresent; // Para datas de filtro passadas

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para buscar transações com filtros opcionais.")
public class TransacaoRequest {

	@Schema(description = "ID opcional da moeda de origem para filtrar transações.", example = "1")
	private Long moedaOrigemId;

	@Schema(description = "ID opcional da moeda de destino para filtrar transações.", example = "2")
	private Long moedaDestinoId;

	@Schema(description = "ID opcional de uma moeda para filtrar transações onde ela é a moeda de origem OU destino.", example = "3")
	private Long moedaId; // Filtra por moedaOrigemId OU moedaDestinoId

	@Schema(description = "Data inicial (inclusive) para filtrar transações. Formato AAAA-MM-DD.", example = "2024-01-01")
	@PastOrPresent(message = "A data inicial não pode ser uma data futura.")
	private LocalDate dataInicial;

	@Schema(description = "Data final (inclusive) para filtrar transações. Formato AAAA-MM-DD.", example = "2024-12-31")
	@PastOrPresent(message = "A data final não pode ser uma data futura.")
	private LocalDate dataFinal;

	@Schema(description = "ID opcional do produto para filtrar transações relacionadas.", example = "101")
	private Long produtoId;

	@Schema(description = "ID opcional do reino para filtrar transações relacionadas aos produtos desse reino.", example = "50")
	private Long reinoId;

}