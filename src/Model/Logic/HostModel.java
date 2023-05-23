package Model.Logic;

import Model.Data.Board;
import Model.Data.Tile;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class HostModel extends PlayerModel implements Observer<HostServer> {
    private HashMap<Integer,Player> connectedPlayers;
    private HostServer hostServer;
    private Board board;
    private Tile[][] prevBoard;
    private Tile.Bag bag;

    private static HostModel hostModel;
    //methods

    public void addNewPlayer(Socket socket){

    }

    public static HostModel getHost(){
        return null;
    }
    private void completeTiles(Player player, int numOfTiles){}

    @Override
    public void tryPlaceWord(String word, int col, int row, boolean isVertical) {

    }

    @Override
    public void takeTileFromBag() {

    }

    @Override
    public void passTurn() {

    }

    @Override
    public void challenge(String word) {

    }

    @Override
    public void setBoardStatus(Tile[][] board) {

    }

    @Override
    public Tile[][] getBoardStatus() {
        return new Tile[0][];
    }

    @Override
    public int getNumberOfTilesInBag() {
        return 0;
    }

    @Override
    public int getCurrentPlayerId() {
        return 0;
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

    }
}
