package com.srm.wefin.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RegraConversaoRequest {

	private Long produtoId;

	private Long moedaOrigemId;

	private Long moedaDestinoId;

	private BigDecimal fatorAjuste;
}