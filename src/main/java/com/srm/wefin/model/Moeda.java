package com.srm.wefin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Moeda {

	private Long id;

	private String nome;

	private String simbolo;
}