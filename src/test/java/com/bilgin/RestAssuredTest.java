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

  @SneakyThrows
  private void assertUserData(ListUsers.UserData userData) {
    assertThat(userData.getEmail().contains("@reqres.in")).isTrue();
    Allure.addAttachment("avatar", "image", userData.getAvatar().openStream(), ".jpg");
  }
}
