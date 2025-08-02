package com.srm.wefin.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxaCambio {

	private Long id;

	private Moeda moedaOrigem;

	private Moeda moedaDestino;

	private BigDecimal taxa;

	private LocalDateTime dataHora;
}