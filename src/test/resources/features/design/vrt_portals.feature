Feature: Visual Regression Testing for Empire Life

  @VRT @EN
  Scenario: Visual design check for the English home page
    Given I launch the browser
    And I navigate to "https://www.empire.ca/"
    Then the page layout should match the baseline "empire-home-en.png"

  @VRT @FR
  Scenario: Visual design check for the French home page
    Given I launch the browser
    And I navigate to "https://www.empire.ca/fr"
    Then the page layout should match the baseline "empire-home-fr.png"

  @VRT @EN
  Scenario: Visual design check for the English search page
    Given I launch the browser
    And I navigate to "https://www.empire.ca/search"
    Then the page layout should match the baseline "empire-search-en.png"

  @VRT @FR
  Scenario: Visual design check for the French search page
    Given I launch the browser
    And I navigate to "https://www.empire.ca/fr/search"
    Then the page layout should match the baseline "empire-search-fr.png"
