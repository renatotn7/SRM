package com.srm.wefin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegraConversaoResponse {

	private Long id;

	private String nomeProduto;

	private BigDecimal fatorAjuste;

	private LocalDate dataVigencia;

}