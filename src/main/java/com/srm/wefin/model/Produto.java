package com.srm.wefin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Produto {

	private Long id;

	private String nome;

	private Reino reino;
}
