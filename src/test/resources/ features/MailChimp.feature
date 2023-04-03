Feature: Mailchimp
  #Testfallen skall gå att köra igen utan att behöva förändra koden i testfallen och så att testfallen fortfarande ger rätt resultat:klar
  #Testfallen skall skapas genom en och enbart en Scenario Outline ifrån en feature-fil:klar
  #Testfallen skall köras på minst två brows-rar:klar
  #Skapa minst en privat metod som använder sig av explicit wait:klar
  #Verifiering skall ske på varje scenario med minst en Junit assert:klar
  #feature-filen skall vara skapad enligt BDD och innehålla en tydlig beskrivning:klar
  #feature-filen skall kopplas till Selenium Webdriver-kod som utför testandet:klar
  #Skapa användare – allt går som förväntat:klar
  #Skapa användare – långt användarnamn (mer än 100 tecken):klar
  #Skapa användare – användare redan upptagen:klar
  #Skapa användare – e-mail saknas:klar
  #sidan kan ha liknade mail och lös men inte samma användnamn:klar
  Scenario Outline: testing
    Given i have opened a <browser>
    And i have written a <email>
    And i have a username <condition>
    And i have written my <password>
    And i press the sign up button
    And my sign in should show <wantedResult>

    Examples:
      | browser | email              | condition | password         | wantedResult |
      | chrome  | username@email.com | normal    | Secretpassword1! | pass         |
      | chrome  | username@email.com | long      | Secretpassword2! | fail         |
      | edge    | username@email.com | existing  | Secretpassword3! | fail         |
      | chrome  |                    | normal    | Secretpassword4! | fail         |