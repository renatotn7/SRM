package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransacaoResponse {

	private Long id;

	private String nomeProduto;

	private String nomeMoedaOrigem;

	private String nomeMoedaDestino;

	private BigDecimal valorOriginal;

	private BigDecimal valorFinal;

	private LocalDateTime dataHora;

	private BigDecimal taxaAplicada;

	private BigDecimal fatorAjusteAplicado;
}