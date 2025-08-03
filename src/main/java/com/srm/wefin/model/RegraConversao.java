package com.srm.wefin.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegraConversao {

	private Long id;

	// Alterado de Long produtoId para o objeto Produto
	private Produto produto;

	private BigDecimal fatorAjuste;

	private LocalDate dataVigencia;
}