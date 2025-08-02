package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaxaCambioRequest {

	private Long moedaOrigemId;

	private Long moedaDestinoId;

	private BigDecimal taxa;

	private LocalDateTime dataHora;
}