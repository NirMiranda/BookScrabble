package Model.Logic;

import Model.Data.Tile;
import Model.Data.Word;

import java.util.HashMap;
import java.util.List;

//this is the interface extenders -> host/guest model.
public interface Module {
    public void tryPlaceWord(String word, int col,int row, boolean isVertical);
    public void passTurn();
    public void challenge(String word);

    public char[][] getBoardStatus();
    public int getNumberOfTilesInBag();
    public int getCurrentPlayerIndex();

    public HashMap<Integer,String> getPlayersNumberOfTiles();
    public List<Tile> getMyTiles();

}