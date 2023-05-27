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
    private HashMap<Integer, String> playersNumberOfTiles = new HashMap<>();
    private static HostServer hostServer;
    private Board board;
    private Tile[][] prevBoard;
    private Tile.Bag bag;
    public int currentPlayerIndex;
    //    public HashMap<Integer,Integer> turnsMap; // Map from index to ID
    private List<Player> turnsOrder;
    boolean isMyTurn = false;


    public HashMap<Integer,Integer> playersScores;

    public HostModel(HostServer hostServer) { //when the game is started
        this.connectedPlayers = new HashMap<>();
        myPlayer.id=0;
        connectedPlayers.put(0,myPlayer);
        this.board = new Board();
        this.prevBoard = this.board.getTile();
        this.bag = Tile.Bag.getBag();
//        this.turnsMap=new HashMap<>();
        this.playersScores=new HashMap<>();
        turnsOrder = new ArrayList<>();

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
        return connectedPlayers.size()+1;
    }

    public static HostModel getHost() {
        if (hostModel == null) {
            hostModel = new HostModel(hostServer);
        }
        return hostModel;
    }

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {
        StringBuilder sb = new StringBuilder();
        Socket myServer = hostServer.sendToMyServer("Q", word);
        Scanner scan = null;
        try {
            scan = new Scanner(myServer.getOutputStream().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Boolean answerFromServer = Boolean.getBoolean(scan.next());
        sb.append(word).append(",").append(row).append(",").append(col).append(isVertical).append(answerFromServer);
        if(answerFromServer){
            wordIsValid(sb.toString(), myPlayer.id);
        }
    }

    @Override
    public void passTurn() {
        this.currentPlayerIndex = (this.currentPlayerIndex+1)%connectedPlayers.size();
        hostServer.updateAllClient(myPlayer.id+";"+"passTurn;"+currentPlayerIndex);
        if(myPlayer.id==turnsOrder.get(currentPlayerIndex).id){
            isMyTurn =true;
        }
    }

    @Override
    public void challenge(String word) {
        Socket myServer = hostServer.sendToMyServer("C", word);
        Scanner scan = null;
        try {
            scan = new Scanner(myServer.getOutputStream().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Boolean answerFromServer = Boolean.getBoolean(scan.next());
        if(answerFromServer){
            this.playersScores.replace(myPlayer.id,playersScores.get(myPlayer.id)+10);//challenge success = +10 points
        }
        else this.playersScores.replace(myPlayer.id,playersScores.get(myPlayer.id)-10);//challenge fail = -10 points
    }

    public void setBoardStatus(Tile[][] board) {
        prevBoard = this.board.getTile();
        for(int i=0;i<15;i++ ){
            for (int j=0;j<15;j++){
                this.board.getTile()[i][j] = board[i][j];
            }
        }
    }
    public char[][] getBoardStatus(){
        return parseBoardStatusToChar(boardParser(this.board));
    }

    @Override
    public int getNumberOfTilesInBag() {
        return bag.getSize();
    }

    @Override
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    public HashMap<Integer, Integer> getPlayersScores() {
        return this.playersScores;
    }

    @Override
    public HashMap<Integer, String> getPlayersNumberOfTiles() {
        return playersNumberOfTiles;
    }

    @Override
    public List<Tile> getMyTiles() {
        return myPlayer.getTiles();
    }
    private void setPlayersNumberOfTiles(){
        for(Integer id:this.connectedPlayers.keySet())
        this.playersNumberOfTiles.put(id,String.valueOf(connectedPlayers.get(id).tiles.size()));
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
                break;
            case "getBoardStatus":
                boardParser(board);
                break;
            case "getNumberOfTilesInBag":
                setNumberOfTilesInBag();
                break;
            case "getCurrentPlayerIndex":
                getCurrentPlayerIndex();
                break;
            case "leaveGame":
                disconnectGuest(id);
                break;
            case "tryPlaceWord":
                wordIsValid(args,id);
                break;
            case "playerConnected" :
                addNewPlayer(hostServer.clientsMap.get(id));
                break;
            case "challenge":
                challengeScoreUpdate(id,args);
        }
    }

    private void challengeScoreUpdate(int id,String response) {
        String[] args = response.split(",");
        if (args[args.length - 1].equals("true")) {
            this.playersScores.replace(id, playersScores.get(id) + 10);//successful challenge
        }
        else this.playersScores.replace(id, playersScores.get(id) + 10);//unsuccessful challenge
        hostServer.updateAllClient(scoreParser(playersScores));
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

    private void setNumberOfTilesInBag(){
        String numberOfTilesInBag = String.valueOf(bag.getQuantities());
        hostServer.updateAllClient(myPlayer.id + ";" + "setNumberOfTilesInBag" + numberOfTilesInBag);
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
    public void startGame(){
        if(this.connectedPlayers.size()==4)
        setGameOrder();
        hostServer.updateAllClient(boardParser(board));
        hostServer.updateAllClient(scoreParser(playersScores));
        hostServer.updateAllClient(myPlayer.id + ";setCurrentPlayerIndex;" + currentPlayerIndex);
        hostServer.updateAllClient(myPlayer.id + ";setNumberOfTilesInBag;" +bag.getQuantities());
    }

    public void wordIsValid(String response, int id) {
        String[] args = response.split(",");
        if (args[args.length - 1].equals("true")) {
            String myWord = args[0];
            int row = Integer.parseInt(args[1]);
            int col = Integer.parseInt(args[2]);
            Boolean isVert = Boolean.parseBoolean(args[3]);
            List<Tile> t = new ArrayList<>();
            for (char c : myWord.toCharArray()) {
                t.add(Tile.Bag.getBag().getTile(c));
            }
            Word w = new Word((Tile[]) t.toArray(), row, col, isVert);
            int score = board.tryPlaceWord(w);
            if (score > 0) {
                connectedPlayers.get(id).score += score;
            }
            setBoardStatus(board.getTile());
            removeTileFromHand(myWord, id);
            if(bag.getSize()>0) {
                fillHand(connectedPlayers.get(id));
            }
            else setPlayersNumberOfTiles();
            playersScores.replace(id, playersScores.get(id) + score);
            setCurrentPlayerIndex();
            hostServer.updateAllClient(boardParser(board));
            hostServer.updateAllClient(scoreParser(playersScores));
            hostServer.updateAllClient(myPlayer.id + ";setCurrentPlayerIndex;" + currentPlayerIndex);
            hostServer.updateAllClient(myPlayer.id + ";setNumberOfTilesInBag;" + bag.getQuantities());
        }
        hostServer.sendToPlayerMessage(id,"tryPlaceWord","false");
    }



    private void removeTileFromHand(String myWord, int id){
        for (int i = 0; i < myWord.length(); i++) {
            if (connectedPlayers.get(id).getTiles().contains(myWord.charAt(i))) {
                connectedPlayers.get(id).tiles.remove(myWord.charAt(i));
            }
        }
    }

    private char[][] parseBoardStatusToChar(String response) {
        char[][] myBoard = new char[15][15];
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


}