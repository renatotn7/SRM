package com.srm.wefin;

import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;

public class BaseTest {

	@BeforeClass
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
		RestAssured.basePath = "/api/v1";
	}
}