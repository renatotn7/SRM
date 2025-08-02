package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaxaCambioResponse {

	private Long id;

	private String nomeMoedaOrigem;

	private String nomeMoedaDestino;

	private BigDecimal taxa;

	private LocalDateTime dataHora;
}