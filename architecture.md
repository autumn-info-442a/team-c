# Architecture

## Models

**GameModel**
- This component is a model that stores all of the prompts the game has requested from the player(s) and all of the answers each player has given to the game.
- The model resides on the server.
- The **ResponseController** communicates with the model. It communicates the following:
    - The **ResponseController** can ask the **GameModel** to store a player response paired with a corresponding prompt for the current Mad Libs template.
    - The **ResponseController** can ask the **GameModel** for all of the stored prompt and response pairs for the current Mad Libs template.
    - The **ResponseController** can ask the **GameModel** for the story text of the current Mad Libs template.
- The **GameController** communicates with the model. It communicates the following:
    - The **GameController** can check with the **GameModel** to see if all the prompts have been fulfilled, signalling the game is over.
- The **OptionModel** communicates with the model. It communicates the following:
    - The **OptionModel** can be joined via query to the **GameModel** to get the template for the selected Mad Libs.

**OptionModel**
- This component is a model that stores the collection of MadLibs templates that a user can select from in order to start a game. Each template contains the story text of the Mad Libs as well as the prompts for words that are needed to fill in the template.
- The model resides on the server.
- The **GameModel** communicates with the model. It communicates the following:
    - The **GameModel** can be joined via query to the **OptionModel** to get the template for the selected Mad Libs.

**SessionModel**
- This component is a model that stores all of the user sessions.
- The model resides on the server.
- The **SessionController** communicates with the model. It communicates the following:
    - The **SessionController** can ask the **SessionModel** to store a session.

**LobbyModel**
- This component is a model that stores all of the game lobbies.
- The model resides on the server.
- The **LobbyController** communicates with the model. It communicates the following:
    - The **LobbyController** can ask the **LobbyModel** to store a lobby.
    - The **LobbyController** can ask the **LobbyModel** to store the players that have joined the lobby.

**FilterModel**
- This component is a model that stores explicit words used in prompt filtering.
- This model resides on the server.
- The **ResponseController** communicates with the model. It communicates the following:
    - The **ResponseController** can receive the list of explicit words from the **FilterModel**.

## Views

**LobbyView**
- This component is a view where the player can set their name and have the option to either create a new lobby or input a code to join an existing lobby.
- This view resides on the client.
- The **LobbyController** communicates with the view. It communicates the following:
    - The **LobbyController** can receive the players that have joined the lobby from the **LobbyView**.
- The **SessionController** communicates with the view. It communicates the following:
    - The **SessionController** can receive the name of the session owner as user input from the **LobbyView**.

**OptionView**
- This component is a view that displays the different Mad Libs titles a host player can choose to play.
- This view resides on the client.
- The **GameController** communicates with the view. It communicates the following:
    - The **GameController** can receive the title the host player has selected from the **OptionView**.
    - The **GameController** can start the game when the user chooses to from the **OptionView**.
- The **LobbyController** communicates with the view. It communicates the following:
    - The **LobbyController** can send the list of players that have joined the lobby to the **OptionView**.

**PromptView**
- This component is a view that displays the prompt that the player needs to input.
- This view resides on the client.
- The **ResponseController** communicates with the view. It communicates the following:
    - The **ResponseController** can receive the responses as user input from the **PromptView**.

**ResultView**
- This component is a view that displays the result of the Mad Libs story and asks if the player would like to play again.
- This view resides on the client.
- The **ResponseController** communicates with the view. It communicates the following:
    - The **ResponseController** can send a combination of the Mad Libs template with all the responses from user inputs to the **ResultView**.
- The **GameController** communicates with the view. It communicates the following:
    - The **GameController** can signal that the game is over, allowing the **ResultView** to load.
    - The **GameController** can reset the game state when the **ResultView** says that the host player has chosen to replay the game.

## Controllers

**ResponseController**
- This component is a controller that asks to store a prompt and response pair corresponding to the current mad libs and also asks for all of the stored prompt and response pairs.
- This controller resides on the client.
- The **GameModel** communicates with the controller. It communicates the following:
    - The **GameModel** can store the prompt and response pair that the **ResponseController** sends to it.
    - The **GameModel** can send all the stored prompt and response pairs to the **ResponseController**.
    - The **GameModel** can send the story text of the current Mad Libs template to the **ResponseController**.
- The **FilterModel** communicates with the controller. It communicates the following:
    - The **FilterModel** can send the list of explicit words to the **ResponseController** to censor the responses received.
- The **PromptView** communicates with the controller. It communicates the following:
    - The **PromptView** can send the responses as user input to the **ResponseController**.
- The **ResultView** communicates with the model. It communicates the following:
    - The **ResultView** can display all the inputs received from the **ResponseController**.

**GameController**
- This component is a controller that controls the state of the game, such as the Mad Libs story chosen, when the game has started, when the game has finished, when the game is replayed, and whose turn it is.
- This controller resides on the client.
- The **GameModel** communicates with the controller. It communicates the following:
    - The **GameModel** can show the **ResponseController** if all the prompts have been fulfilled, signalling the game is over.
- The **OptionView** communicates with the controller. It communicates the following:
    - The **OptionView** can send the Mad Libs title the host player has chosen to the **GameController.**
    - The **OptionView** can ask the **GameController** to start the game when the host player chooses to start the game.
- The **ResultView** communicates with the controller. It communicates the following:
    - The **ResultView** can load when the **GameController** signals that the game is over.
    - The **ResultView** can ask the **GameController** to reset the game state when the host player chooses to replay the game.

**SessionController**
- This component is a controller that asks to store a session.
- This controller resides on the client.
- The **SessionModel** communicates with the controller. It communicates the following:
    - The **SessionModel** can store a session that the **SessionController** sends to it.
- The **LobbyView** communicates with the model. It communicates the following:
    - The **LobbyView** can send the name of the session owner as user input to the **SessionController**.

**LobbyController**
- This component is a controller that asks to store a lobby and the player(s) within it.
- This controller resides on the client.
- The **LobbyView** communicates with the controller. It communicates the following:
    - The **LobbyView** sends the players that have joined the lobby to the **LobbyController**.
- The **LobbyModel** communicates with the controller. It communicates the following:
    - The **LobbyModel** can store the lobby that the **LobbyController** sends to it.
- The **OptionView** communicates with the controller. It communicates the following:
    - The **OptionView** can display the list of players it receives from the **LobbyController**.

## Stubs

**GameModel**
```
game (
    id int,
    option_id int,
    prompt varchar,
    response varchar
);
```
**OptionModel**

```
option (
  id int,
  title varchar,
  story varchar,
  prompt varchar
);

```
**SessionModel**
```
session (
  id int,
  name varchar,
  duration time
);
```
**LobbyModel**
```
lobby (
  id int,
  room_code varchar,
  player_list_id int
);
```
**FilterModel**
```
filter (
  id int,
  word varchar
);

```
**LobbyView**
```
<input type=”text” id=”name” />
<input type=”text” id=”room_code” />
```
**OptionView**
```
<input type=”button” id=”title” />
<input type=”button” id=”start” />
<ul id=”player_list”>
	<!-- retrieve list of players and display here -->
	<li></li>
</ul>
```
**PromptView**
```
<input type=”text” id=”response” />
```
**ResultView**
```
<h1>”Mad Libs Title”</h1>
<p id=”final_mad_libs”>
<!-- retrieve and display finished mad libs result -->
</p>
<input type=”button” id=”replay” />
```
**ResponseController**
```
function storeResponse(prompt, response) { //string, string
  // TODO Replace with actual algorithm
}
function getAllResponses(title) { //string
  // TODO Replace with actual algorithm
  return responses; //map {“prompt”:”response”}
}
function getTemplate(title) { // string
  // TODO Replace with actual algorithm
  return template; // string
}
function filterWords(response) { //string
  // TODO Replace with actual algorithm
  return censoredResponse; //string
}
function buildResult(title) { //string
  template = getTemplate(title);
  responses = getAllResponses(title);
  for response in responses {
    filterWords(response);
  }
  // TODO Replace with actual algorithm
  return result; //string
}
```
**GameController**
```
function startGame(gameId, title) { //int, string
  // TODO Replace with actual algorithm
  return gameState; //struct
}
function resetGame(gameId) { //int
  // TODO Replace with actual algorithm
  return gameState; //object
}
function getTurn(gameId) {
  // TODO Replace with actual algorithm
  return sessionId; //string
}
function checkEndGame(gameId) {
  // TODO Replace with actual algorithm
	return gameOver; // boolean
}
```
**SessionController**
```
function createSession(name) { //string
  // TODO Replace with actual algorithm
  return sessionId; //int
}
```
**LobbyController**
```
function createLobby(sessionId) { //string
  // TODO Replace with actual algorithm
  return roomCode; //string
}
function addPlayerToLobby(sessionId, lobbyId) { //int, int
  // TODO Replace with actual algorithm
}
function getPlayers(lobbyId) {
  // TODO Replace with actual algorithm
  return players; //list [“sessionid”]
}
```
