package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

import io.restassured.http.ContentType;

public class ScoreControllerRA {	
	
	private Long nonExistingMovieId;
	
	private String clientUsername;
	private String clientPassword;
	
	private String clientToken;
	
	private Map<String, Object> postScoreInstance;
	
	@BeforeEach
	void setUp() throws Exception {
				
		nonExistingMovieId = 99L;
		
		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);	
		
		postScoreInstance = new HashMap<>();
		postScoreInstance.put("movieId", 1);
		postScoreInstance.put("score", 4);
		

	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
						
		JSONObject newScore = new JSONObject(postScoreInstance);
		
		given()	
		.header("Content-type", "application/json")
        .header("Authorization", "Bearer " + clientToken)
        .body(newScore)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
    .when()
        .put("/scores/{movieId}", nonExistingMovieId)
    .then()
        .statusCode(404);       
			
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
	
		postScoreInstance.put("movieId", null);
		
		JSONObject newScore = new JSONObject(postScoreInstance);
		
		given()	
		.header("Content-type", "application/json")
        .header("Authorization", "Bearer " + clientToken)
        .body(newScore)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
    .when()
        .put("/scores")
    .then()
        .statusCode(422)
        .body("errors.message[0]", equalTo("Campo requerido"));
		
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		
		postScoreInstance.put("score", -3);
		
		JSONObject newScore = new JSONObject(postScoreInstance);
		
		given()	
		.header("Content-type", "application/json")
        .header("Authorization", "Bearer " + clientToken)
        .body(newScore)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
    .when()
        .put("/scores")
    .then()
        .statusCode(422)
        .body("errors.message[0]", equalTo("Valor mínimo 0"));
	}
}
