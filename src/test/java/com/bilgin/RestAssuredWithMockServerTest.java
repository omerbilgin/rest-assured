package com.bilgin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.mockserver.model.HttpRequest.request;

import io.netty.handler.codec.http.HttpMethod;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import java.util.Objects;
import javax.annotation.Nullable;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public class RestAssuredWithMockServerTest {
  private static final String MOCK_SERVER_VERSION = MockServerClient.class.getPackage().getImplementationVersion();
  private static final DockerImageName DEFAULT_IMAGE_NAME =
      DockerImageName.parse("mockserver/mockserver").withTag(String.format("mockserver-%s", MOCK_SERVER_VERSION));
  @Nullable private static MockServerClient mockServerClient = null;

  @Rule
  public static final MockServerContainer mockServerContainer =
      new MockServerContainer(DEFAULT_IMAGE_NAME);

  @BeforeAll
  static void beforeTest() {
    mockServerContainer.start();
    mockServerClient =
        new MockServerClient(
            mockServerContainer.getHost(), mockServerContainer.getServerPort());
    RestAssured.baseURI =
        mockServerContainer.getHost() + ":" + mockServerContainer.getServerPort();
    RestAssured.basePath = "/client";
  }

  @Test
  public void restAssuredWithMockServerTest() {
    setExpectation("{\"name\":\"Richard Thompson\"}");
    var requestSpecification =
        new RequestSpecBuilder()
            .setBaseUri("http://" + mockServerContainer.getHost())
            .setPort(mockServerContainer.getServerPort())
            .setBasePath("/client")
            .addQueryParam("id", "10001")
            .build();
    given()
        .spec(requestSpecification)
        .get()
        .then()
        .assertThat()
        .statusCode(200)
        .body("name", containsString("Richard"));
  }

  static void setExpectation(String jsonBody) {
    Objects.requireNonNull(mockServerClient)
        .when(
            request()
                .withPath("/client")
                .withMethod(HttpMethod.GET.name())
                .withQueryStringParameter("id", "10001"))
        .respond(
            HttpResponse.response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withStatusCode(200)
                .withBody(jsonBody));
  }
}
