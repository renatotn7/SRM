package com.srm.wefin.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConversaoDetalhes {

	private BigDecimal taxaBase;

	private BigDecimal fatorAjuste;
}