package Model.Data;


import java.util.ArrayList;


public class Board {
    int i =0;
    private static Board board = null;
    int num=0;
    Tile[][] table = new Tile[15][15];
    int[][] score = new int[15][15];



    public Board() {
        for (int i = 0; i < 15; i++)//אתחול המערך
            for (int j = 0; j < 15; j++) {

                table[i][j] = null;
                score[i][j] = 0;
            }

        score[7][7] = 5;
//Setting Red BoardSquare
        score[0][0] = (4);
        score[0][7] = (4);
        score[0][14] = (4);
        score[7][0] = (4);
        score[7][14] = (4);
        score[14][0] = (4);
        score[14][7] = (4);
        score[14][14] = (4);

        //Setting Yellow BoardSquare
        score[1][1] = (3);
        score[2][2] = (3);
        score[3][3] = (3);
        score[4][4] = (3);
        score[1][13] = (3);
        score[2][12] = (3);
        score[3][11] = (3);
        score[4][10] = (3);
        score[13][13] = (3);
        score[12][12] = (3);
        score[11][11] = (3);
        score[10][10] = (3);
        score[10][4] = (3);
        score[11][3] = (3);
        score[12][2] = (3);
        score[13][1] = (3);

        //Setting Blue BoardSquare
        score[1][5] = (2);
        score[1][9] = (2);
        score[5][1] = (2);
        score[5][5] = (2);
        score[5][9] = (2);
        score[5][13] = (2);
        score[9][1] = (2);
        score[9][5] = (2);
        score[9][9] = (2);
        score[9][13] = (2);
        score[13][5] = (2);
        score[13][9] = (2);

        //Setting Cyan BoardSquare
        score[0][3] = (1);
        score[0][11] = (1);
        score[2][6] = (1);
        score[2][8] = (1);
        score[3][0] = (1);
        score[3][7] = (1);
        score[3][14] = (1);
        score[6][2] = (1);
        score[6][6] = (1);
        score[6][8] = (1);
        score[6][12] = (1);
        score[7][3] = (1);
        score[7][11] = (1);
        score[8][2] = (1);
        score[8][6] = (1);
        score[8][8] = (1);
        score[8][12] = (1);
        score[11][0] = (1);
        score[11][7] = (1);
        score[11][14] = (1);
        score[12][6] = (1);
        score[12][8] = (1);
        score[14][3] = (1);
        score[14][11] = (1);

    }

    public static Board getBoard() {
        if (board == null)
            board = new Board();
        return board;
    }

    public Tile[][] getTile() {
        Tile[][] copy = new Tile[15][15];
        for (int i = 0; i < table.length; i++) {
            //System.arraycopy(table, 0, copy[i], 0, table.length);
            for (int j = 0;j< table.length;j++){
                copy[i][j]=this.table[i][j];
            }
        }
        return copy;
    }

    public boolean checkLength(Word w) {//Is the word is on the board?
        if (w.getRow() < 0 || w.getRow() > 15)
            return false;
        if (w.getCol() < 0 || w.getCol() > 15)
            return false;
        else {

            if (w.vertical == true) //המילה אנכית
            {
                if (w.t.length > ((table.length) - w.getRow()))
                    return false;
            } else//המילה אופקית
            {
                if (w.t.length > ((table.length) - w.getCol()))
                    return false;
            }
            return true;
        }
    }

    public boolean dictionaryLegal() { //בודק תקינות - אם קיים בספרים
        return true;
    }
    public boolean IsTheTileNotEmpty(Word w) {
        if (w.t != null)
            return true;
        return false;
    }
    public boolean IsFirstWordIsLegal(Word w) {
        if (this.table[7][7] == null)//the star is empty and check if the word has the 7,7 index.
        {
            int row = w.getRow();
            int col = w.getCol();
            for (int i = 0; i < w.t.length; i++) {
                if (col == 7 & row == 7)
                    return true;
                if (w.vertical == true)//אנכית
                    row++;
                else //אופקית
                    col++;
            }
            return false; //the star is empty and the word doesn't have the 7,7 index.
        }
        return true; //the star isn't empty.
    }

    public boolean DoesTheWordLean(Word w) {
        int row = w.getRow();
        int col = w.getCol();
        if ((this.table[7][7] == null) && IsFirstWordIsLegal(w)==true) //first word is lean on the star.
            return true;

        for(int i=0;i<w.t.length;i++) {
            if (w.t[i] == null && table[row][col] != null)//the letter is already on the table, and the word is lean.
                return true;
            else {
                if (w.isVertical() == true){
                    if(col-1>=0) {
                        if (table[row][col - 1] != null)
                            return true;
                    }
                    if(col+1<=14){
                        if(table[row][col+1]!=null)
                            return true;
                    }
                   row++;
                }

                else {
                    if(row-1>=0){
                        if(table[row-1][col]!=null)
                            return true;
                    }

                    if(row+1<=14){
                        if(table[row+1][col]!=null)
                            return true;
                    }
                    col++;
                }
            }
        }
return false;
    }
    public boolean Isreplacement(Word w) {
        if (DoesTheWordLean(w) == false)//There is nothing to check, the word is not  lean.
            return false;
        else {
            int row = w.getRow();
            int col = w.getCol();
                for (int i = 0; i < w.t.length; i++) {
                    if (table[row][col] != null && w.t[i] != null)
                        return false;//not legal
                    if (w.isVertical())
                        row++;
                    else
                        col++;
            }
        }
        return true;//legal
    }

    ArrayList<Word> getWords(Word w) {

        ArrayList<Word> al = new ArrayList<Word>();
        al.add(w);//add the first new word to the list.

        if (table[7][7] == null && IsFirstWordIsLegal(w) == true)
            return al; //if the first word.

        int row = w.getRow();
        int col = w.getCol();
        int start = 0;
        int end = 0;

        for (int i = 0; i < w.t.length; i++) {

                if ((w.vertical) == true) {
                    if(w.t[i]==null&&table[row][col]!=null) {
                        row++;
                        continue;
                    }
                    if (col != 14) {
                        if (table[row][col+1] != null) {
                            col++;
                            while (table[row][col] != null) {
                                end++;
                                col++;
                            }
                        }
                        col=w.getCol();
                    }
                    if (col != 0) {
                        if (table[row][col-1] != null) {
                            col--;
                            while (table[row][col] != null) {
                                start++;
                                col--;
                            }
                        }
                        col=w.getCol();
                    }
                    if (start != 0 && end != 0)
                        al.add(MakeWord(row, w.getCol()-start, start + end + 1, false,w.t[i]));
                    if (start == 0 && end != 0)
                        al.add(MakeWord(row, w.getCol(), end + 1, false,w.t[i]));
                    if (start != 0 && end == 0)
                        al.add(MakeWord(row, w.getCol()-start, start + 1, false,w.t[i]));
                    row++;
                    col=w.getCol();

                } else {//not vertical
                    if(w.t[i]==null&&table[row][col]!=null){
                        col++;
                        continue;
                    }

                    if (row != 14) {
                        if (table[row+1][col] != null) {
                            row++;
                            while (table[row][col] != null) {
                                row++;
                                end++;
                            }
                        }
                        row=w.getRow();
                    }
                    if (col != 0) {
                        if (table[row-1][col] != null) {
                            row--;
                            while (table[row][col] != null) {
                                row--;
                                start++;
                            }
                        }
                        row=w.getRow();
                    }
                    if (start != 0 && end != 0)
                        al.add(MakeWord(w.getRow()-start, col, start + end + 1, true,w.t[i]));
                    if (start == 0 && end != 0)
                        al.add(MakeWord(w.getRow(), col, end + 1, true,w.t[i]));
                    if (start != 0 && end == 0)
                        al.add(MakeWord(w.getRow()-start, col, start + 1, true,w.t[i]));
                    col++;
                    row=w.getRow();
                }
            start = 0;
            end = 0;
        }

return al;
    }

public Word MakeWord(int row, int col,int length,boolean vertical,Tile t) {
        int r=row;
        int c=col;
    Tile[] temp = new Tile[length];
    for (int i = 0; i < length; i++) {
        if(table[r][c]==null){
            temp[i]=t;
        }else{
        temp[i] = table[r][c];}
        if (vertical == true)
            r++;
        else
            c++;
    }
   Word nw=new Word(temp,row,col,vertical);
    return nw;
}

    public boolean boardLegal(Word w) {
        if(table[7][7]==null)//firs word
        {
            return checkLength(w)&& board.IsFirstWordIsLegal(w);
        }

        return ((checkLength(w) == true) && (IsTheTileNotEmpty(w) == true) && (IsFirstWordIsLegal(w) == true) && (Isreplacement(w) == true));

    }

    public int getScore(Word w) {
        int sum = 0;
        int temp = 1;

        int row = w.getRow(), col = w.getCol();
        int tileScore=0;

        for (int i = 0; i < w.t.length; i++) {

            if (w.t[i] == null && table[row][col] != null) {
                tileScore = table[row][col].getValue();
            }
            else {
                tileScore = w.t[i].getValue();
            }
            if (score[row][col] == 5) { //first word.
                if (table[row][col] == null) {
                    temp *= 2;
                }
                sum += tileScore;
            } else if (score[row][col] == 4) {
                temp *= 3;
                sum += tileScore;
            } else if (score[row][col] == 3) {
                temp *= 2;
                sum += tileScore;
            } else if (score[row][col] == 2)
                sum += (3 * tileScore);
            else if (score[row][col] == 1)
                sum += (2 * tileScore);
            else
                sum += tileScore;

            if (w.vertical)
                row++;
            else
                col++;
        }
        sum = sum * temp;
        return sum;
    }

    public int tryPlaceWord(Word w) {
        ArrayList<Word> temp=getWords(w);
        int currentRow = w.getRow(), currentCol = w.getCol();
        int score = 0;

        if (boardLegal(w) == false)
            return 0;
        if(getWords(w).size()==1) {
            score = getScore(temp.get(0));
        }

        else{
            for (int i=0;i<temp.size();i++)
                score+=getScore(temp.get(i));
        }

        for (int i = 0; i < w.t.length; i++) {
            if(w.t[i] != null)
                table[currentRow][currentCol] = w.t[i];

            if (w.vertical)
                currentRow++;
            else
                currentCol++;
        }
        return score;
    }


}
