package Model.Logic;

import Model.Data.Board;
import Model.Data.Tile;

import java.util.HashMap;
import java.util.Observer;

public class HostModel extends PlayerModel implements Observer<HostServer> {
    private HashMap<Integer,Player> connectedPlayers;
    private HostServer hostServer;
    private Board board;
    private Tile[][] prevBoard;
    private Tile.Bag bag;

    private static HostModel hostModel;

    //methods
    public HostModel getHost(){
        return null;
    }
    private void completeTiles(Player player, int numOfTiles){}
}
