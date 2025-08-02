package com.srm.wefin.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transacao {

	private Long id;

	private Produto produto;

	private BigDecimal valorOriginal;

	private Moeda moedaOrigem;

	private BigDecimal valorFinal;

	private Moeda moedaDestino;

	private LocalDateTime dataHora;

	private BigDecimal taxaAplicada;

	private BigDecimal fatorAjusteAplicado;
}