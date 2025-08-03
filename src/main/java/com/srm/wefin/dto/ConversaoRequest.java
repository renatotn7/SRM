package com.srm.wefin.dto;

import java.math.BigDecimal;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Schema(description = "Dados para solicitar uma conversão de moeda entre produtos.")
public class ConversaoRequest {

	@Schema(description = "ID do produto ao qual a conversão se aplica. Deve ser um ID de um produto existente.", example = "101")
	@NotNull(message = "O ID do produto não pode ser nulo.")
	@Positive(message = "O ID do produto deve ser um número positivo.")
	private Long produtoId;

	@Schema(description = "Valor a ser convertido, em sua moeda de origem.", example = "100.50")
	@NotNull(message = "O valor da conversão não pode ser nulo.")
	@DecimalMin(value = "0.01", message = "O valor da conversão deve ser maior que zero.")
	private BigDecimal valor;

	@Schema(description = "ID da moeda de origem da transação. Deve ser um ID de uma moeda existente.", example = "1")
	@NotNull(message = "O ID da moeda de origem não pode ser nulo.")
	@Positive(message = "O ID da moeda de origem deve ser um número positivo.")
	private Long moedaOrigemId;

	@Schema(description = "ID da moeda de destino da transação. Deve ser um ID de uma moeda existente.", example = "2")
	@NotNull(message = "O ID da moeda de destino não pode ser nulo.")
	@Positive(message = "O ID da moeda de destino deve ser um número positivo.")
	private Long moedaDestinoId;

}