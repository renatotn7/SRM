package com.srm.wefin.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RegraConversaoResponse {

	private Long id;

	private String nomeProduto;

	private String nomeMoedaOrigem;

	private String nomeMoedaDestino;

	private BigDecimal fatorAjuste;
}
