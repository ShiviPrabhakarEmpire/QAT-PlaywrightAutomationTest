Feature: Empire Life Search Functionality
  As a website visitor
  I want to use the search bar
  So that I can quickly find specific products

  @EN
  Scenario: Verify search works correctly
    Given I launch the browser
    And I navigate to "https://www.empire.ca/"
    When I search for "Term Life"
    Then I should be redirected to a page containing "search"

  @FR
  Scenario: Verify search works correctly in French
    Given I launch the browser
    And I navigate to "https://www.empire.ca/fr"
    When I search for "Assurance vie temporaire"
    Then I should be redirected to a page containing "search"
