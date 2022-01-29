package com.bilgin;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

import org.junit.jupiter.api.Test;

public class RestAssuredTest {

  private static String baseUri = "http://reqres.in";
  private static  String basePath = "/api/users/2";

  @BeforeAll
  public static void initialize() {
    RestAssured.baseURI = baseUri;
    RestAssured.basePath = basePath;
  }

  @Test
  public void singleUserTest() {
    var httpRequest = given();
    var response = httpRequest.get();
    var body = response.getBody().asString();
    assertTrue(body.contains("janet.weaver@reqres.in"), "String not found");
  }
}
