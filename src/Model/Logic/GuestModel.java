package Model.Logic;

import Model.Data.Tile;

import java.io.IOException;
import java.util.*;


public class GuestModel extends PlayerModel implements Observer {
    private ClientCommunication clientCommunication;
    private int numberOfTilesInBag;
    private int currentPlayerIndex;
    private HashMap<Integer, Integer> playersScores = new HashMap<>();
    private HashMap<Integer, Player> playersConnected = new HashMap<>(); // may not be needed
    private HashMap<Integer, String> playersNumberOfTiles;
    private char[][] myBoard =new char[15][15];
    boolean isMyTurn = false;
    private List<Integer> turnsOrder = new ArrayList<>();

    /**
     * @param clientCommunication connects between the guest to the host server
     */

    public GuestModel(ClientCommunication clientCommunication) {
        this.clientCommunication = clientCommunication;
        clientCommunication.addObserver(this);
        clientCommunication.send(-1, "playerConnected");

        this.playersNumberOfTiles = new HashMap<>(); // Initialize the HashMap here

    }

    /**
     *
     * @param word the word that the user wants to place on the board
     * @param col which col the first letter stands on
     * @param row which row the first letter stands on
     * @param isVertical true = the word goes vertical, false = the word goes horizontal
     */

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {
        clientCommunication.send(myPlayer.id, "tryPlaceWord",word, String.valueOf(row), String.valueOf(col), String.valueOf(isVertical));
    }

    @Override
    public void passTurn() {
        clientCommunication.send(myPlayer.id,"passTurn");
    }

    /**
     *
     * @param word the word that the user wants to challenge on and believes its correct
     */
    @Override
    public void challenge(String word) {
        clientCommunication.send(myPlayer.id, "challenge", word);
    }

    @Override
    public char[][] getBoardStatus() {

        return myBoard;
    }

    @Override
    public int getNumberOfTilesInBag() {
        return numberOfTilesInBag;
    }
    public List<Integer> getTurnsOrder(){
//        clientCommunication.send(myPlayer.id,"getTurnsOrder");
        return turnsOrder;

    }


    @Override
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }


    public HashMap<Integer, Integer> getPlayersScores() {
        return playersScores;
    }

    @Override
    public HashMap<Integer, String> getPlayersNumberOfTiles() {
        return playersNumberOfTiles;
    }

    @Override
    public List<Tile> getMyTiles() {
        return myPlayer.getTiles();
    }

    /**
     *
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method. - a string of the form - id; method name; different arguments
     */
    //protocol- "id;method;arg1,arg2,arg3.."
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            String message = (String) arg;
            String[] parts = message.split(";");

            int id = Integer.parseInt(parts[0]);
            String methodName = parts[1];
            String args = parts[2];

            switch (methodName) {
                case "startGame":
                    handleStartGame();
                    break;
                case "endGame":
                    handleEndGame();
                    break;
                case "setBoardStatus":
                    myBoard = parseBoardStatus(args);
                    break;
                case "setNumberOfTilesInBag":
                    int numberOfTiles = Integer.parseInt(args);
                    setNumberOfTilesInBag(numberOfTiles);
                    break;
                case "setCurrentPlayerIndex":
                    setCurrentPlayerIndex(Integer.parseInt(args));
                    break;
                case "setPlayersScores":
                    HashMap<Integer, Integer> scores = parsePlayersScores(args);
                    setPlayersScores(scores);
                    break;
                case "setPlayersNumberOfTiles":
                    HashMap<Integer, String> numberOfTilesMap = parsePlayersNumberOfTiles(args);
                    setPlayersNumberOfTiles(numberOfTilesMap);
                    break;
                case "addNewPlayer":
                    int connectedPlayerId = Integer.parseInt(args);
                    int connectedPlayerScore = 0; // Set the initial score to 0
                    playersScores.put(connectedPlayerId, connectedPlayerScore);
                    break;
                case "setGameOrder":
                    setGameOrder(args);
                    break;
                case "disconnect":
                    updatePlayerLeft(args);
                    break;
                case  "passTurn":
                    turnPassed();
                    break;
//                case "undo":
//                    myBoard = parseBoardStatus(args);
                case "tryPlaceWord":
                    wrongWordHandler();
                    break;
                default:
                    System.out.println("Unknown method name: " + methodName);
                    break;
            }
        }
    }

    private void wrongWordHandler() {
        System.out.println("Wrong word, try again\n You may challenge the decision\n" +
                " Successful challenge = +10 points\n Unsuccessful challenge = -10 points ");
    }

    private void updatePlayerLeft(String args) {

        playersScores.remove(Integer.parseInt(args));//delete from the players map
        turnsOrder.remove(Integer.parseInt(args));
    }

    private void setGameOrder(String response) {
        String[] playersTurn = response.split(",");

        for (String pair : playersTurn) {
            String[] keyValue = pair.split(":");
            int playerId = Integer.parseInt(keyValue[0]);
            int turn = Integer.parseInt(keyValue[1]);
            turnsOrder.add(turn,playerId);
        }
    }

    private void turnPassed() {
        currentPlayerIndex = (currentPlayerIndex+1)%turnsOrder.size();
    }

    // Helper methods for parsing the responses



    /**
     * @param response the string sent from the server and the protocol is this Tile,Tile,Tile...
     *                 and each Tile represented as - row:col="letter"
     *                 each tile represented in the guestModel as char on 15X15 char matrix;
     * @return the new board as a 15X15 matrix
     */

    private char[][] parseBoardStatus(String response) {

        String[] tiles = response.split(",");

        for (String tile : tiles) {
            String[] tileParts = tile.split("=");

            // Extract the tile position and value
            String[] positionParts = tileParts[0].split(":");
            int row = Integer.parseInt(positionParts[0]);
            int col = Integer.parseInt(positionParts[1]);
            char value = tileParts[1].charAt(0);

            // Create a new Tile object with the value and add it to the board
            myBoard[row][col] = value;

        }

        return myBoard;
    }

    /**
     *
     * @param response the string sent from the server and the protocol is this Player,Player,Player...
     *                 and each Player represented as - id:score
     * @return the new map - key=id and value = score
     */

    private HashMap<Integer, Integer> parsePlayersScores(String response) {
        HashMap<Integer, Integer> playersScores = new HashMap<>();
        String[] playerScoresPairs = response.split(",");

        for (String pair : playerScoresPairs) {
            String[] keyValue = pair.split(":");
            int playerId = Integer.parseInt(keyValue[0]);
            int score = Integer.parseInt(keyValue[1]);
//            playersConnected.get(playerId).score=score;
            playersScores.put(playerId, score);
        }

        return playersScores;
    }

    /**
     * @param response the string sent from the server and the protocol is this Player,Player,Player...
     *                 and each Player represented as - id:amount of tiles
     * @return the new map - key=id and value = amount of tiles as string
     */
    private HashMap<Integer, String> parsePlayersNumberOfTiles(String response) {
        HashMap<Integer, String> playersNumberOfTiles = new HashMap<>();
        String[] playerTilesPairs = response.split(",");

        for (String pair : playerTilesPairs) {
            String[] keyValue = pair.split(":");
            int playerId = Integer.parseInt(keyValue[0]);
            String numberOfTiles = keyValue[1];
            playersNumberOfTiles.put(playerId, numberOfTiles);
        }

        return playersNumberOfTiles;
    }


    // Setter methods

    private void setNumberOfTilesInBag(int numberOfTiles) {
        this.numberOfTilesInBag = numberOfTiles;
    }

    private void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
        if(turnsOrder.get(currentPlayerIndex) == myPlayer.id) {
            isMyTurn = true;
        }
    }

    private void setPlayersScores(HashMap<Integer, Integer> playersScores) {
        this.playersScores = playersScores;
    }

    private void setPlayersNumberOfTiles(HashMap<Integer, String> playersNumberOfTiles) {
        this.playersNumberOfTiles = playersNumberOfTiles;
    }


    private void leaveGame(){
        clientCommunication.send(myPlayer.id,"leaveGame");
        try {
            this.clientCommunication.socket.close();
        } catch (IOException e) { throw new RuntimeException(e);}
    }
    private void handleStartGame() { //may not be needed since host model updates everything he does.
        getBoardStatus();
        getTurnsOrder();
        getCurrentPlayerIndex();
        getMyTiles();
    }

    private void handleEndGame() {
        getBoardStatus();
        try {
            this.clientCommunication.socket.close();
        } catch (IOException e) { throw new RuntimeException(e);}
    }
//    private void undo(){
//
//    }
}