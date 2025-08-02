package com.srm.wefin.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConversaoRequest {

	private String produto;

	private BigDecimal valor;

	private String moedaOrigem;

	private String moedaDestino;
}