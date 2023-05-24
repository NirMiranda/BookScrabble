package Model.Logic;

import Model.Data.Board;
import Model.Data.Tile;

import java.io.IOException;
import java.net.Socket;
import java.util.*;


public class HostModel extends PlayerModel implements Observer {
    private HashMap<Integer, Player> connectedPlayers;
    private HostServer hostServer;
    private Board board;
    private Tile[][] prevBoard;
    private Tile.Bag bag;
    public int currentPlayerId;
    private Set<Player> turnsOrder;

    public HostModel() {
        this.connectedPlayers = new HashMap<>();
        this.board = new Board();
        this.prevBoard = this.board.getTile();
        this.bag = Tile.Bag.getBag();
    }

    private static HostModel hostModel;
    //methods

    public void addNewPlayer(Socket socket) {
        return;
    }


    public static HostModel getHost() {
        if (hostModel == null) {
            hostModel = new HostModel();
        }
        return hostModel;
    }
    private void completeTiles(Player player, int numOfTiles){

    }

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {

    }

    @Override
    public void takeTileFromBag() {

    }

    @Override
    public void passTurn() {
        this.currentPlayerId = (this.currentPlayerId+1)%4;
        hostServer.updateAllClient(myPlayer.id+";"+"passTurn");
    }

    @Override
    public void challenge(String word) {

    }

    @Override
    public void setBoardStatus(Tile[][] board) {

    }

    @Override
    public char[][] getBoardStatus() {
        return new char[0][];
    }

    @Override
    public int getNumberOfTilesInBag() {
        return 0;
    }

    @Override
    public int getCurrentPlayerId() {
        hostServer.updateAllClient(myPlayer.id + ";" + "setCurrentPlayerId" + currentPlayerId);
        return currentPlayerId;
    }

    @Override
    public HashMap<Integer, Integer> getPlayersScores() {
        return null;
    }

    @Override
    public HashMap<Integer, String> getPlayersNumberOfTiles() {
        return null;
    }

    @Override
    public List<Tile> getMyTiles() {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        String updateFromServer = (String)arg;
        String[] parts = updateFromServer.split(";");
        int id= Integer.valueOf(parts[0]);
        String methodName = parts[1];
        String args = parts[2];
        switch (methodName){
            case "passTurn":
                passTurn();
            case "getBoardStatus":
                boardParser(board);
            case "getNumberOfTilesInBag":
                setNumberOfTilesInBag();
            case "getCurrentPlayerId":
                getCurrentPlayerId();
            case "leaveGame":
                disconnectGuest(id);
        }
    }

    private void boardParser(Board board) {
            Tile[][] tiles = board.getTile();
            StringBuilder sb = new StringBuilder();

            for (int row = 0; row < tiles.length; row++) {
                for (int col = 0; col < tiles[row].length; col++) {
                    Tile tile = tiles[row][col];
                    sb.append(row).append(":").append(col).append("=").append(tile.getLetter()).append(",");
                }
            }
            // Remove the trailing comma, if any
            sb.deleteCharAt(sb.length()-1);

        hostServer.updateAllClient(myPlayer.id + ";" + "setBoardStatus" + sb);
    }

    public Socket GetSocketMyServer(){
        return hostModel.GetSocketMyServer();
    }

    private void setCurrentPlayerId(){
        this.currentPlayerId=(this.currentPlayerId+1)%4;
    }
    private void setPlayersScores(){

    }
    private void setPlayersNumberOfTiles(){

    }
    private void setNumberOfTilesInBag(){
        String numberOfTilesInBag = String.valueOf(bag.getQuantities());
        hostServer.updateAllClient(myPlayer.id + ";" + "setNumberOfTilesInBag" + numberOfTilesInBag);
    }
    private void startGame(){

    }
    private void endGame(){
        this.hostServer.updateAllClient(myPlayer.id + ";endGame");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            this.hostServer.myServer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.hostServer.close();
    }
    private void disconnectGuest(int id){
        this.connectedPlayers.remove(id);
        hostServer.updateAllClient(myPlayer.id + ";" + "disconnect" + ";" + id);
    }
    private void undo(){

    }
    private void dealRandomTile(){
        for(Integer id:this.connectedPlayers.keySet()){
            connectedPlayers.get(id).tiles.add(bag.getRand());
        }
    }
    private void setGameOrder(){
        this.connectedPlayers.keySet().stream().sorted()
    }
    private void dealTiles(){

    }


}