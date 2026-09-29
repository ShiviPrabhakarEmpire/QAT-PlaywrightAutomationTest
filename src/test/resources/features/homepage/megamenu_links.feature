Feature: Empire Life Mega Menu Sub-links
  As a website visitor
  I want to access specific sub-category links directly from the mega menu
  So that I can quickly find the exact insurance or investment product

  @EN
  Scenario: Navigate to Term Life Insurance from menu
    Given I launch the browser
    And I navigate to "https://www.empire.ca/"
    When I click on the "Term Life Insurance" link
    Then I should be redirected to a page containing "term-life-insurance"

  @EN
  Scenario: Navigate to Annuities from menu
    Given I launch the browser
    And I navigate to "https://www.empire.ca/"
    When I click on the "Annuities" link
    Then I should be redirected to a page containing "annuities"

  @FR
  Scenario: Navigate to Assurance vie temporaire from menu
    Given I launch the browser
    And I navigate to "https://www.empire.ca/fr"
    When I click on the "Assurance vie temporaire" link
    Then I should be redirected to a page containing "term-life-insurance"

  @FR
  Scenario: Navigate to Rentes from menu
    Given I launch the browser
    And I navigate to "https://www.empire.ca/fr"
    When I click on the "Rentes" link
    Then I should be redirected to a page containing "annuities"
