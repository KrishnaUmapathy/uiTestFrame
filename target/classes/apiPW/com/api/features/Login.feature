#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template

Feature: Title of your feature
  I want to use this template for my feature file



Scenario: Ping env
        Given environment is loaded
        Then ping the environment and status should be success
   
    #Scenario: Login API
    #Given environment is loaded
    #When user logs into API with valid credentials and login is success
    #
    #Scenario: Quick Quote API
    #Given environment is loaded
    #When user logs into API with valid credentials and login is success
    #Then requests for quickQuote
#
    #Scenario: Persist Quote API
    #Given environment is loaded
    #When user logs into API with valid credentials and login is success
    #Then posts the persistQuote
    #
    #Scenario: Persist Quote BankDD API
    #Given environment is loaded
    #When user logs into API with valid credentials and login is success
    #And posts the persistQuote
    #Then Update PersistQuote DD
    #
    #Scenario: Persist Quote Activate
    #Given environment is loaded
    #When user logs into API with valid credentials and login is success
    #And posts the persistQuote
    #Then Update PersistQuote DD
    #And activate the quote
    #
 
