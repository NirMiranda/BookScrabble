package Model.Logic;

import Model.Data.Board;
import Model.Data.Tile;
import Model.Data.Word;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;


public class HostModel extends PlayerModel implements Observer {
    private HashMap<Integer, Player> connectedPlayers;
    private HostServer hostServer;
    private Board board;
    private Tile[][] prevBoard;
    private Tile.Bag bag;
    public int currentPlayerIndex;
    //    public HashMap<Integer,Integer> turnsMap; // Map from index to ID
    private List<Player> turnsOrder;


    public HashMap<Integer,Integer> playersScores;

    public HostModel() {
        this.connectedPlayers = new HashMap<>();
        this.board = new Board();
        this.prevBoard = this.board.getTile();
        this.bag = Tile.Bag.getBag();
//        this.turnsMap=new HashMap<>();
        this.playersScores=new HashMap<>();
    }

    private static HostModel hostModel;
    //methods

    public void addNewPlayer(Socket socket) {
        Player player = new Player(idGenerator(),null,0,null);
        connectedPlayers.put(player.id,player);
        try {
            socket.connect(socket.getRemoteSocketAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int idGenerator(){
        myPlayer.id=0;
        return connectedPlayers.size()+1;
    }

    public static HostModel getHost() {
        if (hostModel == null) {
            hostModel = new HostModel();
        }
        return hostModel;
    }

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {

    }

    @Override
    public void passTurn() {
        this.currentPlayerIndex = (this.currentPlayerIndex+1)%connectedPlayers.size();
        hostServer.updateAllClient(myPlayer.id+";"+"passTurn");
    }

    @Override
    public void challenge(String word) {

    }

    @Override
    public void setBoardStatus(Tile[][] board) {

    }
    public char[][] getBoardStatus(){
        return new char[0][];
    }

    @Override
    public int getNumberOfTilesInBag() {
        return bag.getSize();
    }

    @Override
    public int getCurrentPlayerIndex() {
        hostServer.updateAllClient(myPlayer.id + ";" + "setCurrentPlayerIndex" + currentPlayerIndex);
        return currentPlayerIndex;
    }
    public HashMap<Integer, Integer> getPlayersScores() {
        return null;
    }

    @Override
    public HashMap<Integer, String> getPlayersNumberOfTiles() {
        return null;
    }

    @Override
    public List<Tile> getMyTiles() {
        return myPlayer.getTiles();
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
            case "getCurrentPlayerIndex":
                getCurrentPlayerIndex();
            case "leaveGame":
                disconnectGuest(id);
            case "tryPlaceWord":
                wordIsValid(args,id);
            case "playerConnected" :
                addNewPlayer(hostServer.clientsMap.get(id));
        }
    }

    private String boardParser(Board board) {
        Tile[][] tiles = board.getTile();
        StringBuilder sb = new StringBuilder();
        sb.append(myPlayer.id).append( ";setBoardStatus;");
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                Tile tile = tiles[row][col];
                sb.append(row).append(":").append(col).append("=").append(tile.getLetter()).append(",");
            }
        }
        // Remove the trailing comma, if any
        sb.deleteCharAt(sb.length()-1);
        return  sb.toString();
    }
    private String scoreParser(HashMap<Integer, Integer> playersScores) {
        StringBuilder sb = new StringBuilder();
        sb.append(myPlayer.id).append(";setScoresStatus;");
        for(Integer id:playersScores.keySet()){
            sb.append(id).append(":").append(playersScores.get(id)).append(",");

        }

        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public Socket GetSocketMyServer(){
        return hostModel.GetSocketMyServer();
    }

    private void setCurrentPlayerIndex(){
        this.currentPlayerIndex=(this.currentPlayerIndex+1)%connectedPlayers.size();
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
        this.turnsOrder.remove(id);
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
        dealRandomTile();
        for (int i=0;i<connectedPlayers.size();i++){
            turnsOrder=turnsOrder.stream().sorted((a,b)->{
                return b.tiles.get(0).letter-a.tiles.get(0).letter;
            }).collect(Collectors.toList());
        }
        for (Integer id:this.connectedPlayers.keySet()){
            bag.put(connectedPlayers.get(id).tiles.get(0));
            connectedPlayers.get(id).tiles.remove(0);
        }
        for(int i=0;i<turnsOrder.size();i++){
            fillHand(connectedPlayers.get(i));
            playersScores.put(connectedPlayers.get(i).id,0);
        }

    }
    private void fillHand(Player p){
        for (int i=0;i<7-p.tiles.size();i++){
            p.tiles.add(bag.getRand());
        }

    }
    public void wordIsValid(String response, int id) {
        String[] args = response.split(",");
        if (args[args.length - 1].equals("true")) {
            String myWord = args[0];
            int row=Integer.parseInt(args[1]);
            int col=Integer.parseInt(args[2]);
            Boolean isVert=Boolean.parseBoolean(args[3]);
            List<Tile> t=new ArrayList<>();
            for(char c:myWord.toCharArray()){
                t.add(Tile.Bag.getBag().getTile(c));
            }
            Word w = new Word((Tile[])t.toArray(),row,col,isVert);
            int score=board.tryPlaceWord(w);
            if(score>0){
                connectedPlayers.get(id).score+=score;
            }
            removeTileFromHand(myWord,id);
            fillHand(connectedPlayers.get(id));
            playersScores.replace(id,playersScores.get(id)+score);

        }
        hostServer.updateAllClient(boardParser(board));
        hostServer.updateAllClient(scoreParser(playersScores));
        hostServer.updateAllClient(myPlayer.id + ";setCurrentPlayerIndex;" + currentPlayerIndex++);
        hostServer.updateAllClient(myPlayer.id + ";setNumberOfTilesInBag;" +bag.getQuantities());
    }



    private void removeTileFromHand(String myWord, int id){
        for (int i = 0; i < myWord.length(); i++) {
            if (connectedPlayers.get(id).getTiles().contains(myWord.charAt(i))) {
                connectedPlayers.get(id).tiles.remove(myWord.charAt(i));
            }
        }
    }



}