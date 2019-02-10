Feature: I want to know about newly created repositories
    So that I they may be checked for compliance
    For LargeCorp's legal team

    Scenario: Ensuring that github recieves an appropriate response from us
        Given that the TestInc organisation exists in Github
# The webhook
        When we are told that TestInc has a new repository 
# Respond with a 204
        Then we should respond appropriately 

