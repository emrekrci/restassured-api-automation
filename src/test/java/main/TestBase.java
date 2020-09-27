package main;


import org.junit.Before;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class TestBase {
	public static RequestSpecification requestSpecification;
	
	@Before
	public void setUp() {
		RestAssured.baseURI = "http://ergast.com/api/";
		requestSpecification = RestAssured.given();
	}
}
