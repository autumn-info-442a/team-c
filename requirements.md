# Requirements

## Before game
1. `complete` The system should allow users to set a name before they enter a game lobby. 
2. `complete` The system should allow users to create a game lobby.
3. `complete` The system should allow users to enter an existing lobby by typing in the code to that lobby.
4. `complete` The system should display 5 Mad Lib titles as clickable options to the user so that they can select a game.
    - This is complete, but just wanted to note that it is shown as a dropdown instead of a list of buttons as originally intended. A dropdown forces a user to select an option and is still clickable.
5. `complete` The system should allow the user who created the lobby to start the game once they’ve selected a Mad Lib.
6. `complete` The system should not allow more than 4 users in the same game lobby.
7. `complete` The system should store the Mad Libs templates sourced from the [Mad Libs website](https://www.madlibs.com/) and the team's own creations.
8. `complete` The system should credit the Mad Libs templates that it gets from the [Mad Libs website](https://www.madlibs.com/).
    - This is complete because we didn't use any Mad Libs templates from the website, so we did not have to credit the website.
9. `complete` The system should display Mad Libs templates handpicked by the team in alphabetical order by title.
10. `complete` The system should display to the players which other players are in the game lobby.
    - This is complete, but just wanted to note that it shows as a modal instead of a section on the page as originally intended.

## During game
11. `complete` The system should be able to receive text input from every player in the game.
12. `complete` The system should be able to determine which user’s turn it is to input text.
13. `complete` The system should request input from users when it is their turn.
14. `complete` This system should display to a user when another user is currently taking their turn.
15. `complete` The system should send input information to the server for later use in the final Mad Lib.
16. `complete` The system should not request input from more than one user at a time within a game session.
17. `complete` The system should not request the same input (specific part of speech at a specific point in the Mad Lib) that it has previously requested from any user in the current session.
18. `complete` The system should be able to identify what input (specific part of speech at a specific point in the Mad Lib) to request.
19. `complete` The system should allow text input to be a minimum of 2 characters and a maximum of 25 characters.
20. `complete` The system should block users from submitting inappropriate language (determined by a pre-determined list of words).
21. `complete` The system should give all players at least one chance depending on the length of the Mad Lib to input a part of speech to contribute to the Mad Lib.

## After game
22. `complete` The system should be able to generate the completed Mad Lib using accumulated user inputs.
23. `complete` The system should format user inputs to properly fit within the Mad Lib with regard to appropriate punctuation and capitalization by removing any punctuation and making all words lowercase.
24. `complete` The system should display the final result to all users when all inputs have been received.
25. `??incomplete` The system should allow users to replay Mad Libs with the same game lobby.
    - This was lower priority, so we decided to forgo it as a user is still able to play with the same players by simply creating a new game lobby. It only takes two extra clicks per player versus one.
26. `complete` The system should display a prompt on the screen to encourage users to read the final story to each other.

## Compatibility
27. `complete` The system should allow users to fully interact with it using any standard modern web browser on mobile or desktop.

## Sessions
28. `complete` The system should be able to host one or more game lobbies.
29. `??complete` The system should automatically disconnect a session if all players are idle for at least 20 minutes.
