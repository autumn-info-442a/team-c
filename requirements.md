# Requirements

## Before game
1. The system should allow users to set a name before they enter a game lobby.
2. The system should allow users to create a game lobby.
3. The system should allow users to enter an existing lobby by typing in the code to that lobby.
4. The system should display 5 Mad Lib titles as clickable options to the user so that they can select a game.
5. The system should allow the user to start the game once they’ve selected a Mad Lib.
6. The system should not allow more than 4 users in the same game lobby.

## During game
7. The system should be able to receive text input from every player in the game.
8. The system should be able to determine which user’s turn it is to input text.
9. The system should request input from users when it is their turn.
10. This system should display to a user when another user is currently taking their turn.
11. The system should send input information to the server for later use in the final Mad Lib.
12. The system should not request input from more than one user at a time within a game session.
13. The system should not request the same input (specific part of speech at a specific point in the Mad Lib) that it has previously requested from any user in the current session.
14. The system should be able to identify what input (specific part of speech at a specific point in the Mad Lib) to request.
15. The system should allow text input to be a minimum of 2 characters and a maximum of 25 characters.
16. The system should give all players at least one chance depending on the length of the Mad Lib to input a part of speech to contribute to the Mad Lib.

## After game
17. The system should be able to generate the completed Mad Lib using accumulated user inputs.
18. The system should format user inputs to properly fit within the Mad Lib with regard to appropriate punctuation and capitalization by making removing any punctuation and making all words lowercase.
19. The system should display the final result to all users when all inputs have been received.
20. The system should censor inappropriate language with asterisks.
21. The system should allow users to replay the same activity with the same game lobby.
22. The system should display a prompt on the screen to encourage users to read the final story to each other.

## Compatibility
23. The system should allow users to fully interact with it using any browser on any device.

## Sessions
24. The system should be able to host one or more game lobbies.
25. The system should automatically disconnect a session if all players are idle for at least 20 minutes.
