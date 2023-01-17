Feature: Everything is fine

  Even when there is a fire burning around you, everything is fine.

  Background:
    Given you are having coffee
    And there is a fire around you

  Scenario: You're still alright

    When you are still alright
    Then everything is fine

  Scenario: You start to feel it

    When you start to feel it
    Then panic