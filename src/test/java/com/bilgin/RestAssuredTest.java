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
    var user = new User("John Doe", "Software Engineer", null, null, null);

    var response = given().contentType(ContentType.JSON).body(user)
            .post("/users");

    int statusCode = response.getStatusCode();
    assertEquals(statusCode, 201);

    var createdUser = response.getBody().as(User.class);
    assertNotNull(createdUser);
    assertNotNull(createdUser.name());
    assertNotNull(createdUser.job());
    assertNotNull(createdUser.id());
  }

  @Test
  @Description("Assertion for updating an existing user")
  @Feature("RestAssured")
  @Owner("Bilgin")
  public void updateUserTest() {
      var user = new User("Jane Doe", "Product Manager", null, null, null);

      var response = given().contentType(ContentType.JSON).body(user)
              .put("/users/2");

      var statusCode = response.getStatusCode();
      assertEquals(statusCode, 200);

      var responseBody = response.getBody();
      var jsonResponse = new JSONObject(responseBody);

    var updatedUser = response.getBody().as(User.class);
    assertNotNull(updatedUser);
    assertEquals(updatedUser.name(), "Jane Doe");
    assertEquals(updatedUser.job(), "Product Manager");
    assertNotNull(updatedUser.updatedAt());
  }

  @SneakyThrows
  private void assertUserData(ListUsers.UserData userData) {
    assertThat(userData.getEmail().contains("@reqres.in")).isTrue();
    Allure.addAttachment("avatar", "image", userData.getAvatar().openStream(), ".jpg");
  }

  public record User(String name, String job, String id, String createdAt, String updatedAt) {}
}
