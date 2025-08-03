package com.srm.wefin.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import com.srm.wefin.BaseTest;
import com.srm.wefin.dto.ConversaoRequest;

import io.restassured.http.ContentType;

public class ConversaoControllerTest extends BaseTest {

	@Test
	public void testRealizarConversaoComSucesso() {
		ConversaoRequest request = new ConversaoRequest();
		request.setMoedaOrigemId(1L); // Assumindo que 1 é um ID válido para uma moeda existente
		request.setMoedaDestinoId(2L); // Assumindo que 2 é um ID válido para outra moeda existente
		request.setValor(new BigDecimal("100.00"));
		request.setProdutoId(1L); // Assumindo que 1 é um ID válido para um produto existente

		given().contentType(ContentType.JSON).body(request).when().post("/conversoes").then().statusCode(201).body("transacaoId", notNullValue()) // Verifica se transacaoId não é nulo
				.body("transacaoId", not(emptyString())) // Verifica se transacaoId não é uma string vazia
				.body("dataHora", notNullValue()) // Verifica se dataHora não é nulo
				.body("produto", notNullValue()) // Verifica se produto (nome) não é nulo
				.body("produto", not(emptyString())) // Verifica se produto (nome) não é uma string vazia
				.body("valorOriginal", equalTo(100.00F)) // Usa .00F para float/double se o JSON vier assim. 
				.body("moedaOrigem", notNullValue()) // Verifica se moedaOrigem (nome) não é nulo
				.body("moedaOrigem", not(emptyString())) // Verifica se moedaOrigem (nome) não é uma string vazia
				.body("valorFinal", notNullValue()) // Verifica se valorFinal não é nulo
				.body("valorFinal", greaterThan(0.00F)) // Verifica se valorFinal é maior que zero (assumindo conversão real)
				.body("moedaDestino", notNullValue()) // Verifica se moedaDestino (nome) não é nulo
				.body("moedaDestino", not(emptyString())) // Verifica se moedaDestino (nome) não é uma string vazia
				.body("detalhesConversao", notNullValue()); // Verifica se detalhesConversao (objeto) não é nulo
	}

	@Test
	public void testRealizarConversaoMoedasInvalidas() {
		ConversaoRequest request = new ConversaoRequest();
		request.setMoedaOrigemId(999L);
		request.setMoedaDestinoId(998L);
		request.setValor(new BigDecimal("100.00"));
		request.setProdutoId(1L);

		given().contentType(ContentType.JSON).body(request).when().post("/conversoes").then().statusCode(404).body("message", notNullValue());
	}

	@Test
	public void testRealizarConversaoValorZero() {
		ConversaoRequest request = new ConversaoRequest();
		request.setMoedaOrigemId(1L);
		request.setMoedaDestinoId(2L);
		request.setValor(BigDecimal.ZERO);
		request.setProdutoId(1L);

		given().contentType(ContentType.JSON).body(request).when().post("/conversoes").then().statusCode(400).body("message", notNullValue());
	}
}