@parity
Feature: Parity Testing for French Empire Site

  Scenario: Verify structural and visual parity for French Homepage
    Given I prepare a parity test named "homepage-fr-parity"
    When I load the base URL "https://www.empire.ca/fr" and target URL "https://www.empire.ca/fr"
    Then the DOM structure should be identical
    And the visual layout should be identical

  Scenario: Verify structural and visual parity for French Search Page
    Given I prepare a parity test named "search-fr-parity"
    When I load the base URL "https://www.empire.ca/fr/search" and target URL "https://www.empire.ca/fr/search"
    Then the DOM structure should be identical
    And the visual layout should be identical
