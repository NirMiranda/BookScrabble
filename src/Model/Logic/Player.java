package Model.Logic;

import Model.Data.Tile;

import java.util.List;

public class Player {
    int id;
    String name;
    int score;
    List<Tile> tiles;

    public int getId() {
        return 0;
    }

    public String getName() {
        return null;
    }

    public int getScore() {
        return 0;
    }

    public List<Tile> getTiles() {
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }
    public void addTile(Tile tile){}
}
