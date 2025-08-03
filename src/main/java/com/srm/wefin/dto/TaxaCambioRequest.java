package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criar ou atualizar uma taxa de câmbio entre duas moedas.")
public class TaxaCambioRequest {

	@Schema(description = "Identificador único da moeda de origem para esta taxa de câmbio.", example = "1")
	@NotNull(message = "O ID da moeda de origem não pode ser nulo.")
	private Long moedaOrigemId;

	@Schema(description = "Identificador único da moeda de destino para esta taxa de câmbio.", example = "2")
	@NotNull(message = "O ID da moeda de destino não pode ser nulo.")
	private Long moedaDestinoId;

	@Schema(description = "A taxa de conversão entre a moeda de origem e a moeda de destino. Deve ser um valor positivo.", example = "5.25")
	@NotNull(message = "A taxa de câmbio não pode ser nula.")
	@DecimalMin(value = "0.000001", message = "A taxa de câmbio deve ser um valor positivo e maior que zero.")
	private BigDecimal taxa;

	@Schema(description = "A data a partir da qual esta taxa de câmbio é válida. Se nula, usa a data atual.", example = "2025-08-01")
	@NotNull(message = "A data de registro não pode ser nula.")
	@PastOrPresent(message = "A data de registro deve ser uma data e hora no passado ou presente.")
	private LocalDate dataRegistro;
}
