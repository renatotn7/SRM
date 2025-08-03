package com.srm.wefin.dto;

import java.time.LocalDateTime;
import java.util.List; // Para erros de validação

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO padrão para respostas de erro da API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErroResponse {

	private LocalDateTime timestamp;

	private Integer status;

	private String error; // Nome do erro HTTP (e.g., "Bad Request", "Conflict")

	private String message; // Mensagem geral do erro

	private List<String> details; // Detalhes específicos, usado para erros de validação (lista de "campo: mensagem")

	private String path; // URI da requisição que causou o erro

	// Construtor auxiliar para erros mais simples sem detalhes
	public ErroResponse(LocalDateTime timestamp, Integer status, String error, String message, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}
}