package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criar ou atualizar uma regra de conversão de moeda para um produto.")
public class RegraConversaoRequest {

	@Schema(description = "Identificador único do produto ao qual esta regra de conversão se aplica.", example = "1")
	@NotNull(message = "O ID do produto não pode ser nulo.")
	private Long produtoId;

	@Schema(description = "Fator numérico usado para a conversão da moeda (ex: 0.5 para 50%, 2.0 para o dobro). Deve ser maior que zero.", example = "1.5")
	@NotNull(message = "O fator de ajuste não pode ser nulo.")
	@DecimalMin(value = "0.000001", message = "O fator de ajuste deve ser um valor positivo e maior que zero.")
	private BigDecimal fatorAjuste;

	@Schema(description = "Data a partir da qual esta regra de conversão entra em vigor. Deve ser a data atual ou uma data futura.", example = "2025-09-01")
	@NotNull(message = "A data de vigência não pode ser nula.")
	@FutureOrPresent(message = "A data de vigência deve ser a data atual ou uma data futura.")
	private LocalDate dataVigencia;
}