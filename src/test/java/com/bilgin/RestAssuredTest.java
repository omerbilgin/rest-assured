package com.bilgin;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
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
  @Description("Assertion for single user response from real test api")
  @Feature("RestAssured")
  @Owner("Bilgin")
  public void singleUserTest() {
    assertTrue(
        given().get("users/2").getBody().asString().contains("janet.weaver@reqres.in"),
        "String not found");
  }
}
