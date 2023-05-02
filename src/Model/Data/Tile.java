package Model.Data;

import java.util.Objects;

public class Tile {
    public final char letter;
    public final int score;



    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;

    }

    public char getLetter() {
        return letter;
    }

    public int getValue() {
        return score;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return (letter == tile.getLetter() && score == tile.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag {
        int sum=98;
        private static Bag b=null;
        final int[] amount = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1}; //כמות מכל אות קבוע אפשרי
        int[] arrAmountLetter = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1}; //כמות מכל אות
        int[] arrValue = new int[]{1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10}; //ערך האות
        char[] arrL = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        Tile[] t;

        private Bag() {
             t = new Tile[26];

            for (int i = 0; i < 26; i++) //מאתחל את מערך ה TILE
            {
                t[i] = new Tile(arrL[i], arrValue[i]);
            }
        }


        public Tile getRand() {
            int rand = (int)(Math.random()*26);
            while (arrAmountLetter[rand] == 0) {
                rand = (int)(Math.random()*26);
            }

            arrAmountLetter[rand]--;
            sum--;
            return t[rand];

            }


        public Tile getTile(char a) { //החזרת אריח לפי אות נתונ ה
            for (int i=0;i<26;i++) {
                if (t[i].getLetter() == a) {
                    if (arrAmountLetter[a-'A']!=0)
                        return t[i];
                }
            }
            return null; // אם לא ניתן להוציא
        }

        public void put(Tile c) //מוסיף לכמות האריחים אחד
        {
            int indexTile=c.getLetter() - 'A';

            if(arrAmountLetter[indexTile]+1<=amount[indexTile]) {
                arrAmountLetter[indexTile]++;
                sum++;
            }
            else
                return;
        }

        public int getSize() //מחזירה את כמות האריחים במערך
        {
            return sum;
        }

        public int[] getQuantities()
        {
            int[]copy=new int[26];
            System.arraycopy(arrAmountLetter,0,copy,0, arrAmountLetter.length);
            return copy;
        }



        public static Bag getBag(){
            if (b==null)
            {
                b=new Bag();
            }
            return b;
        }

    }
}





