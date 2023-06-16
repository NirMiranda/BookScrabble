package Model.Logic;

import Model.Data.Tile;

import java.util.List;

public class Player {
    int id;
    String name;

    int index;
    int score;
    List<Tile> tiles;

    /**
     * Constructs a new Player object with the given name.
     * Initializes the score to 0 and creates an empty hand of tiles.
     * @param name The name of the player.
     */

    public Player(int id, String name, int score, List<Tile> tiles) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.tiles = tiles;
    }


    public int getId() {
        return this.id;
    }

    /**
     * The isRackEmpty method checks if the player's rack and bag are both empty.
     * @return True if the player's rack and bag are empty, false otherwise.
     */
    public boolean isTilesEmpty() {
        return Tile.Bag.getBag().getSize() == 0 && this.getTiles().isEmpty();    }

    /**
     * The getIndex method returns the index of the current node.
     * @return The index value.
     */
    public int getIndex() {
        return index;
    }


    /**
     * The setIndex method sets the index of a player.
     * @param newIndex The index to set for the player.
     */
    public void setIndex(int newIndex) {
        index = newIndex;
    }

    /**
     * The getName method returns the name of the person.
     * @return The name of the player.
     */
    public String getName() {
        return this.name;
    }

    /**
     * The getScore method returns the score of the player.
     * @return The score of the player.
     */
    public int getScore() {
        return this.score;
    }

    public List<Tile> getTiles() {
        return this.tiles;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * The setScore method sets the score of a player.
     * @param newScore The score to set for the player.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * The updateHand method is used to update the player's hand when receiving a new hand from the server.
     * @param newHand The new hand of tiles to update the player's hand with.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The updateHand method is used to update the player's hand when receiving a new hand from the server.
     * @param newHand The new hand of tiles to update the player's hand with.
     */

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }
    public void addTile(Tile tile){}
}
