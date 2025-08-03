package com.srm.wefin.dto;

import java.time.LocalDate;

import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Data
@Schema(description = "Parâmetros de busca para consultar o histórico de transações.")
public class HistoricoSearchRequest {

	@Schema(description = "ID de qualquer moeda envolvida na transação (origem ou destino).", example = "3")
	@Positive(message = "O ID da moeda deve ser um número positivo.")
	private Long moedaId; // Novo campo documentado e validado

	@Schema(description = "ID da moeda de origem da transação.", example = "1")
	@Positive(message = "O ID da moeda de origem deve ser um número positivo.")
	private Long moedaOrigemId;

	@Schema(description = "ID da moeda de destino da transação.", example = "2")
	@Positive(message = "O ID da moeda de destino deve ser um número positivo.")
	private Long moedaDestinoId;

	@Schema(description = "Data inicial para o período de busca (formato YYYY-MM-DD).", example = "2024-01-01")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@PastOrPresent(message = "A data inicial não pode ser uma data futura.")
	private LocalDate dataInicial;

	@Schema(description = "Data final para o período de busca (formato YYYY-MM-DD).", example = "2024-12-31")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@PastOrPresent(message = "A data final não pode ser uma data futura.")
	private LocalDate dataFinal;

	@Schema(description = "ID do produto associado à transação.", example = "101")
	@Positive(message = "O ID do produto deve ser um número positivo.")
	private Long produtoId;

}