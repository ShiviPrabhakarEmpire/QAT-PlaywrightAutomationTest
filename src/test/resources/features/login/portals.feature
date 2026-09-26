Feature: Empire Life Login Portals
  As a customer or advisor
  I want to access my specific login portal
  So that I can manage my account

  @EN
  Scenario: Customer navigates to MyEmpire portal
    Given I launch the browser
    And I navigate to the Empire page "https://www.empire.ca/"
    When I click the Log in button
    And I select "MyEmpire - Insurance and Investments" from the dropdown
    Then a new tab should open pointing to the MyEmpire portal

  @FR
  Scenario: Customer navigates to MyEmpire portal in French
    Given I launch the browser
    And I navigate to the Empire page "https://www.empire.ca/fr"
    When I click the Log in button
    And I select "MonEmpire pour l’assurance et les placements" from the dropdown
    Then a new tab should open pointing to the MyEmpire portal
