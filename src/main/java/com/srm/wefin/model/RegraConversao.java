package com.srm.wefin.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegraConversao {

	private Long id;

	private Long produtoId;

	private BigDecimal fatorAjuste;
}