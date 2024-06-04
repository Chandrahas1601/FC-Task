Feature: Validate user tasks completion percentage for FanCode city users

  Scenario: Verify users from FanCode city have more than 50% task completion
    Given User has the todo tasks
    And User belongs to the city FanCode
    Then User Completed task percentage should be greater than 50%
