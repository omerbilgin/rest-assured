package com.bilgin;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reqres.model.ListUsers;

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

  @SneakyThrows
  @Test
  @Description("Assertion for user list response from real test api")
  @Feature("RestAssured")
  @Owner("Bilgin")
  @Attachment()
  public void userListTest() {
    var usersList =
        ListUsers.fromResponse(given().queryParam("page", 2).get("users").getBody().asString());
    assertNotNull(usersList);

    assertFalse(usersList.getData().isEmpty());
    assertThat(usersList.getData().size()).isGreaterThan(0);
    usersList.getData().forEach(userData -> assertUserData(userData));
  }

  @Test
  @Description("Assertion for creating a new user")
  @Feature("RestAssured")
  @Owner("Bilgin")
  public void createUser() {
    var requestParams = new JSONObject();
    requestParams.put("name", "John Doe");
    requestParams.put("job", "Software Engineer");

    var response = given().contentType(ContentType.JSON).body(requestParams.toString())
            .post("/users");

    var statusCode = response.getStatusCode();
    assertEquals(statusCode, 201);

    var jsonResponse = new JSONObject(response.getBody().asString());

    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.has("name"));
    assertTrue(jsonResponse.has("job"));
    assertTrue(jsonResponse.has("id"));
    assertEquals(jsonResponse.getString("name"), "Jane Doe");
    assertEquals(jsonResponse.getString("job"), "Software Engineer");
  }

  @Test
  @Description("Assertion for updating an existing user")
  @Feature("RestAssured")
  @Owner("Bilgin")
  public void updateUserTest() {
      var requestParams = new JSONObject();
      requestParams.put("name", "Jane Doe");
      requestParams.put("job", "Product Manager");

      var response = given().contentType(ContentType.JSON).body(requestParams.toString())
              .put("/users/2");

      var statusCode = response.getStatusCode();
      assertEquals(statusCode, 200);

      var responseBody = response.getBody().asString();
      var jsonResponse = new JSONObject(responseBody);

      assertNotNull(jsonResponse);
      assertTrue(jsonResponse.has("name"));
      assertTrue(jsonResponse.has("job"));
      assertTrue(jsonResponse.has("updatedAt"));

      assertEquals(jsonResponse.getString("name"), "Jane Doe");
      assertEquals(jsonResponse.getString("job"), "Product Manager");
  }

  @SneakyThrows
  private void assertUserData(ListUsers.UserData userData) {
    assertThat(userData.getEmail().contains("@reqres.in")).isTrue();
    Allure.addAttachment("avatar", "image", userData.getAvatar().openStream(), ".jpg");
  }
}
