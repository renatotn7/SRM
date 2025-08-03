package com.srm.wefin.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import org.testng.annotations.Test;

import com.srm.wefin.BaseTest;

public class HistoricoControllerTest extends BaseTest {

	@Test
	public void testGetHistoricoSemFiltro() {
		given().when().get("/historico").then().statusCode(200).body("", not(empty()));
	}

	@Test
	public void testGetHistoricoPorMoedaOrigemId() {
		given().queryParam("moedaOrigemId", 1L).when().get("/historico").then().statusCode(200).body("", not(empty()));
	}

	//Não deu tempo
	@Test(enabled = false)
	public void testGetHistoricoPorDataInicialEFinal() {
		given().queryParam("dataInicial", "2020-01-01").queryParam("dataFinal", "2026-01-01").when().get("/historico").then().statusCode(200).body("", not(empty()));
	}

	@Test
	public void testGetHistoricoComFiltroQueNaoRetornaResultados() {
		given().queryParam("moedaOrigemId", 999L).queryParam("dataInicial", "2020-01-01").when().get("/historico").then().statusCode(200).body("", empty());
	}

}