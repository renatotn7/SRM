package com.srm.wefin.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
class ConversaoDetalhes {

	private BigDecimal taxaBase;

	private BigDecimal fatorAjuste;
}