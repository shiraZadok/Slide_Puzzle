//
// Created by shira on 01/06/2020.
//
import java.util.Arrays;
import java.util.Vector;

//Reverse order because the code moves the blank square.
enum MoveDIR {R, D, L, U};

public class Board {
    //Static variable to count the number of the product board
    public static int boardCount=0;
    // Storing the square in a matrix
    protected Square[][] board;
    //Num of rows in board
    private int row;
    //Num of columns in board
    private int column;
    //Size of board = row*column
    private int sizeOfBoard;
    //Key for every state of board
    private String key;
    //The position of the blank square
    private Position blankPos;
    //Save the old Position of the blank square
    private Position oldBlankPos;
    //Vector that stored all the num of the black square
    private Vector<Integer> Black;
    //Vector that stored all the num of the red square
    private Vector<Integer> Red;
    //Save the father of the board
    private Board father;
    //Save the last move to get this board
    private String lastMove;
    //The costs to produce this board
    private int cost;
    //Production number
    private int productNum;
    //Check if this board in the path (for IDA*, DFBnB), true = mark is "out", false = "not out".
    private boolean mark;

    //Default Constructors
    public Board(){
        this.row=0;
        this.column=0;
        this.sizeOfBoard=0;
        this.blankPos = new Position();
        this.oldBlankPos = new Position();
        this.Black = new Vector<>();
        this.Red = new Vector<>();
        this.father = null;
        this.lastMove = "";
        this.key=getKey();
        this.cost=0;
        this.productNum=boardCount++;
        this.mark = false;
    }

    //Copy Constructors
    public Board (Board copy){
        this.row=copy.row;
        this.column=copy.column;
        this.sizeOfBoard=copy.sizeOfBoard;
        this.Red=copy.Red;
        this.Black=copy.Black;
        this.blankPos=new Position();
        this.oldBlankPos=copy.blankPos;
        this.father=copy;
        lastMove ="";
        this.cost=copy.cost;
        this.board = new Square[this.row][this.column];
        for (int i= 0; i< this.row; i++)
            this.board[i] = Arrays.copyOf(copy.board[i], copy.board[i].length);
        this.key="";
        this.productNum=boardCount++;
        this.mark = false;
    }

    public int getProductNum() { return productNum; }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getSizeOfBoard() {
        return sizeOfBoard;
    }

    public void setKey(String key) { this.key = key; }

    public boolean getMark() { return mark; }

    public void setMark(boolean mark) { this.mark = mark; }

    public String getLastMove() { return lastMove; }

    public Board getFather() { return father; }

    public void setSizeOfBoard(int sizeOfBoard) { this.sizeOfBoard = sizeOfBoard; }

    public Vector<Integer> getBlack() { return Black; }

    public void setBlack(Vector<Integer> black) { Black = black; }

    public Vector<Integer> getRed() { return Red; }

    public void setRed(Vector<Integer> red) { Red = red; }

    public int getCost() { return cost; }

    public void setBlankPos(int r, int c) {
        this.blankPos.setPosition(r,c);
    }

    /**
     * This method creates a string that represents the number arrangement in the board
     * @return the key
     */
    public String getKey() {
        this.key="";
        for (int i=0; i<this.row; i++)
            for (int j=0; j<this.column; j++)
                key+= String.valueOf(this.board[i][j].getNum())+",";
        return key;
    }

    /**
     * This method print the board
     */
    public void printBoard(){
        for (Square[] i:board){
            for (Square j: i)
                System.out.print(j.getNum()!= sizeOfBoard ?
                        j.getNum()+String.valueOf(j.getColor()) +"\t" : "_\t");
            System.out.println();
        }
    }

    /**
     * This is "Operator function" - Move the blank square to the permissible directions
     * @param direction the try direction for move on the board
     * @return new board after the move direction done
     */
    public Board Operator(MoveDIR direction){
        Position newPos = new Position();
        switch (direction) {
            case U:
                newPos.setPosition(blankPos.getRow() - 1, blankPos.getColumn());
                break;
            case D:
                newPos.setPosition(blankPos.getRow() + 1, blankPos.getColumn());
                break;
            case R:
                newPos.setPosition(blankPos.getRow(), blankPos.getColumn() + 1);
                break;
            case L:
                newPos.setPosition(blankPos.getRow(), blankPos.getColumn() - 1);
                break;
        }
        //Check that the new position is not out of bound
        if (newPos.getRow() < 0 || newPos.getRow() >= this.row || newPos.getColumn() < 0 || newPos.getColumn() >= this.column)
            return null;
        //Check if the new position is a black square
        if (this.board[newPos.getRow()][newPos.getColumn()].getColor() == COLOR.B)
            return null;
        //Check to avoid prevent reverse operation
        if (this.oldBlankPos.getRow() != -1 && this.oldBlankPos.equals(newPos))
            return null;
        //If all the error check throw the new position is correct
        Board newBoard = new Board(this);
        newBoard.switchSquare(this.blankPos,newPos);
        newBoard.blankPos=new Position(newPos);
        //Change the last move according the number of the square
        String opDirection = OppositeDirection(direction);
        Square moveSquare = this.board[newPos.row][newPos.column];
        //Update the last move according to the move square
        newBoard.lastMove = String.valueOf(moveSquare.getNum())+opDirection;
        //Update the incremental price of the board
        newBoard.cost += moveSquare.getCost();
        //Change the key of the board
        newBoard.setKey(newBoard.getKey());
        return newBoard;
    }

    /**
     * This method return the opposite direction of the last move (because the blank square move).
     * @param direction the try direction for move on the board
     * @return String with the opposite direction
     */
    private String OppositeDirection(MoveDIR direction) {
        String opDir = "";
        switch (direction) {
            case U: opDir= "D";break;
            case D: opDir= "U";break;
            case R: opDir= "L";break;
            case L: opDir= "R";break;
        }
        return opDir;
    }

    /**
     * This method switch between two square in the board according to the new locations
     * @param blankPos the old position of the blank square
     * @param newPos for the blank square
     */
    private void switchSquare(Position blankPos, Position newPos) {
        Square temp = this.board[newPos.row][newPos.column];
        this.board[newPos.row][newPos.column] = this.board[blankPos.row][blankPos.column];
        this.board[blankPos.row][blankPos.column] = temp;
    }

    /**
     * Check if all the black square in the correct position.
     * @return true if the board is Solvable, else false
     */
    public boolean isSolvable() {
        for (int i = 0; i < this.row ; i++) {
            for (int j = 0; j < this.column ; j++) {
                if (this.board[i][j].getColor() == COLOR.B) {
                    int g_row = (this.board[i][j].getNum() - 1) / this.column;
                    int g_col = (this.board[i][j].getNum() - 1) % this.column;
                    if (g_row != i || g_col != j) return false;
                }
            }
        }
        return true;
    }
}