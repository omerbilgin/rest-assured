package com.bilgin;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RestAssuredTest {

  @BeforeAll
  public static void initialize() {
    RestAssured.baseURI = "http://reqres.in";
    RestAssured.basePath = "/api";
  }

  @Test
  public void singleUserTest() {
    assertTrue(
        given().get("users/2").getBody().asString().contains("janet.weaver@reqres.in"),
        "String not found");
  }
}
