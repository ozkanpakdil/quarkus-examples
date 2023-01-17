package com.example.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class HealthEndpointTest {

    private final String BASE_URL = "v1/";

    @Test
    void ping_pong() {
        when()
            .get(BASE_URL + "ping")
            .then()
            .statusCode(200)
            .body(is("pong"));
    }
}
