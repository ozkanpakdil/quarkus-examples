package com.example.test.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static io.restassured.RestAssured.when;

public class EverythingIsFineSteps {

    private final String BASE_URL = "v1/";

    @Given("you are having coffee")
    public void you_are_having_coffee() {
        assert(true);
    }

    @Given("there is a fire around you")
    public void there_is_a_fire_around_you() {
        assert(true);
    }

    @When("you are still alright")
    public void you_are_still_alright() {
        assert(true);
    }

    @Then("everything is fine")
    public void everything_is_fine() {
        when()
            .get(BASE_URL + "health")
            .then()
            .statusCode(200);
    }

    @When("you start to feel it")
    public void you_start_to_feel_it() {
        assert(true);
    }

    @Then("panic")
    public void panic() {
        assert(true);
    }
}
