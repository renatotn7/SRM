package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaxaCambioResponse {

	private Long id;

	private String nomeMoedaOrigem; // Nome da moeda de origem (ex: "Ouro Real")

	private String simboloMoedaOrigem; // Símbolo da moeda de origem (ex: "OR")

	private String nomeMoedaDestino; // Nome da moeda de destino (ex: "Tibar")

	private String simboloMoedaDestino; // Símbolo da moeda de destino (ex: "T")

	private BigDecimal taxa;

	private LocalDateTime dataRegistro;
}