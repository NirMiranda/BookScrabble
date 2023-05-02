package Model.Data;

import java.util.Arrays;
import java.util.Objects;

public class Word {


    Tile []t;
    int row;
    int col;
    boolean vertical;

    public Word(Tile[] t, int row, int col, boolean vertical) {
        this.t = t;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Arrays.equals(t, word.t);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(row, col, vertical);
        result = 31 * result + Arrays.hashCode(t);
        return result;
    }




    public Tile[] getWord() {
        return t;
    }


    public int getRow() {
        return row;
    }


    public int getCol() {
        return col;
    }


    public boolean isVertical() {
        return vertical;
    }


}
