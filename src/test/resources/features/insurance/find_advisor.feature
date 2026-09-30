@insurance @EN
Feature: Find an Advisor Workflow
  As a prospective customer
  I want to find an advisor on empire.ca
  So that I can get professional help with my life insurance needs

  Scenario: Fill out Find an Advisor contact form
    Given I launch the browser
    When I navigate to "https://www.empire.ca/insurance/buy-life-insurance/find-an-advisor"
    And I fill out the Find an Advisor form with my contact details
    And I consent to the use of my personal information
    Then the Search Now button should be enabled
