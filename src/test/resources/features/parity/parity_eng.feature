@parity
Feature: Parity Testing for English Empire Site

  Scenario: Verify structural and visual parity for English Homepage
    Given I prepare a parity test named "homepage-eng-parity"
    When I load the base URL "https://www.empire.ca" and target URL "https://www.empire.ca"
    Then the DOM structure should be identical
    And the visual layout should be identical

  Scenario: Verify structural and visual parity for English Search Page
    Given I prepare a parity test named "search-eng-parity"
    When I load the base URL "https://www.empire.ca/search" and target URL "https://www.empire.ca/search"
    Then the DOM structure should be identical
    And the visual layout should be identical
