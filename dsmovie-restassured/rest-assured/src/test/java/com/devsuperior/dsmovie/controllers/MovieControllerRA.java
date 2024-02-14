package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

public class MovieControllerRA {
	
	private Long existingId;
	private Long nonExistingId;
	
	private String adminUsername;
	private String adminPassword;
	private String clientUsername;
	private String clientPassword;
	
	private String adminToken;
	private String clientToken;
	private String invalidToken;
	
	private String titleMovie;
	
	private Map<String, Object> postMovieInstance;
	
	@BeforeEach
	void setUp() throws Exception {
		
		baseURI = "http://localhost:8080";
		
		titleMovie = "The Witcher";	
		
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";
		
		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		invalidToken = adminToken + "xpto";
		
		postMovieInstance = new HashMap<>();
		postMovieInstance.put("title", "Test Movie");
		postMovieInstance.put("score", 0.0);
		postMovieInstance.put("count", 0);
		postMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");

	}
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
		
		given()
			.get("/movies")
		.then()
			.statusCode(200)
			.body("content.title", hasItem("The Witcher"));
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {		
		
		given()
		.get("/movies?page=0&name={titleMovie}", titleMovie)
	.then()
		.statusCode(200)
		.body("content.title[0]", equalTo("The Witcher"));
		
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
		
		existingId = 1L;		
		
		given()
			.get("/movies/{id}", existingId)
		.then()
			.statusCode(200)
			.body("id", is(1));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		
		nonExistingId = 99L;		
		
		given()
			.get("/movies/{id}", nonExistingId)
		.then()
			.statusCode(404);
			
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
		
		postMovieInstance.put("title", "");
		
		JSONObject newMovie = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newMovie)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(422)
			.body("errors.message[0]", equalTo("Tamanho deve ser entre 5 e 80 caracteres"));			
		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
				
		JSONObject newMovie = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newMovie)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(403);			
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		
		JSONObject newMovie = new JSONObject(postMovieInstance);
		
		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.body(newMovie)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/movies")
		.then()
			.statusCode(401);
	}
}
