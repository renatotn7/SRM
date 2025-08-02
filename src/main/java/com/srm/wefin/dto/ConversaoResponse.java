package com.srm.wefin.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConversaoResponse {

	private String transacaoId;

	private String dataHora;

	private String produto;

	private BigDecimal valorOriginal;

	private String moedaOrigem;

	private BigDecimal valorFinal;

	private String moedaDestino;

	private ConversaoDetalhes detalhesConversao;
}