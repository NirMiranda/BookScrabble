package Model.Logic;

import Model.Data.Tile;
import Model.Data.Word;

import java.util.HashMap;
import java.util.List;

//this is the interface extenders -> host/guest model.
public interface Module {
    public void tryPlaceWord(String word, int col,int row, boolean isVertical);
    public void takeTileFromBag();
    public void passTurn();
    public void challenge(String word);

    public void setBoardStatus(Tile[][] board);

    public char[][] getBoardStatus();
    public int getNumberOfTilesInBag();
    public int getCurrentPlayerId();
//    public HashMap<Integer,Integer> getPlayersScores();
    public HashMap<Integer,String> getPlayersNumberOfTiles();
    public List<Tile> getMyTiles();

}
