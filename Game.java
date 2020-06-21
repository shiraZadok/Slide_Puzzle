//
// Created by shira on 01/06/2020.
//
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Game {
    //Which kind of algorithm to solve the board
    private String Algorithm;
    //"with time" = true, "no time" =false
    private boolean time;
    //"with open" = true, "no open" =false
    private boolean openList;
    //Board game
    protected Board board;

    //Default Constructors
    public Game(){
        this.Algorithm = "";
        this.time = false;
        this.openList = false;
        this.board = new Board();
    }

    //Copy Constructors
    public Game(Game copy, Board newBoard) {
        this.Algorithm = copy.Algorithm;
        this.time = copy.time;
        this.openList = copy.openList;
        this.board = newBoard;
    }

    public String getAlgorithm() {
        return Algorithm;
    }

    public void setAlgorithm(String algorithm) {
        Algorithm = algorithm;
    }

    public boolean getTime() {
        return time;
    }

    public void setTime(boolean time) {
        this.time = time;
    }

    public boolean getOpenList() {
        return openList;
    }

    /**
     * This method initializes game properties by input.txt
     * @param input the input.txt
     */
    public void init(Scanner input){
        this.setAlgorithm(input.nextLine());
        this.setTimeS(input.nextLine());
        this.setOpenListS(input.nextLine());
        this.setSizeBoardS(input.nextLine());
        this.setBlackS(input.nextLine());
        this.setRedS(input.nextLine());
        this.setBoardStartPosition(input);
    }

    /**
     * This method check if this game should print the time depended on the input.txt
     * "with time" = true, "no time" = false
     * @param time represent a string withe the chose
     */
    public void setTimeS(String time){
        if(time.charAt(0)=='w') this.time = true;
        else this.time = false;
    }

    /**
     * This method check if this game should print the open list depended on the input.txt
     * "with open" = true, "no open" = false
     * @param openList represent a string withe the chose
     */
    public void setOpenListS(String openList){
        if(openList.charAt(0)=='w') this.openList = true;
        else this.openList = false;
    }

    /**
     * This method convert the size of board from the input.txt to int
     * @param size is a string that represent the size of board
     */
    public void setSizeBoardS (String size){
        int row = Character.getNumericValue(size.charAt(0));
        this.board.setRow(row);
        int column = Character.getNumericValue(size.charAt(2));
        this.board.setColumn(column);
        this.board.setSizeOfBoard(row*column);
        //After we have the size of the board we can initialize him.
        this.board.board = new Square[row][column];
    }

    /**
     * This method convert the string that represent all black square to int vector
     * @param black represents all black squares
     */
    public void setBlackS(String black){
        String arr = black.substring(6);
        if(arr.length()>0) {
            arr = arr.substring(1);
            String[] temp = arr.split(",");
            for (String num : temp) this.board.getBlack().add(Integer.parseInt(num));
        }
    }

    /**
     * This method convert the string that represent all red square to int vector
     * @param red is string that represents all black squares
     */
    public void setRedS(String red){
        String arr = red.substring(4);
        if(arr.length()>0) {
            arr = arr.substring(1);
            String[] temp = arr.split(",");
            for (String num : temp) this.board.getRed().add(Integer.parseInt(num));
        }
    }

    /**
     * This method accepts the initial layout of the board
     * @param input is string that represents the start position of the board
     */
    public void setBoardStartPosition(Scanner input){
        int row=0, column =0;
        while (input.hasNext()) {
            String[] temp = input.nextLine().split(",");
            for (String num : temp) {
                if (!num.equals("_")) {
                    this.board.board[row][column] = new Square();
                    int theNum = Integer.parseInt(num);
                    this.board.board[row][column].setNum(theNum);

                    if (this.board.getBlack().contains(theNum)) {
                        this.board.board[row][column].setColor(COLOR.B);
                        this.board.board[row][column].setCost(0);
                    } else if (this.board.getRed().contains(theNum)) {
                        this.board.board[row][column].setColor(COLOR.R);
                        this.board.board[row][column].setCost(30);
                    } else {
                        this.board.board[row][column].setColor(COLOR.G);
                        this.board.board[row][column].setCost(1);
                    }
                }
                //The blank square represented by the size of the board NxM
                else {
                    this.board.board[row][column] = new Square();
                    this.board.board[row][column].setNum(board.getSizeOfBoard());
                    this.board.setBlankPos(row, column);
                }
                column++;
            }
            column = 0;
            row++;
        }
        //After the board is initialized, a key can be given
        this.board.setKey(this.board.getKey());
    }

    /**
     * This method make a output text with the result of the game
     * @param goal - the result board
     */
    public void makeOutput (Board goal, long time){
        try {
            String timeS = String.format("%.3f",(time *Math.pow(10, -3)));
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            //If goal = null, the algorithm did not succeed find path
            if (goal == null) { writer.write("no path\r\nNum: " + Board.boardCount);
                //***************************************************************
                System.out.println("no path\r\nNum: " + Board.boardCount +"\r\n");
                //***************************************************************
                 }
            else{
                Board runner = goal;
                String moves="";
                do {
                    moves = runner.getLastMove()+"-"+moves;
                    runner = runner.getFather();
                }
                while (runner!=null);
                //delete the last "-" in moves
                moves = moves.substring(1,moves.length()-1);
                writer.write(moves+"\r\nNum: " + Board.boardCount +"\r\nCost: " +goal.getCost()
                +(this.time ? "\r\n"+timeS+" seconds":""));

                //***************************************************************
                System.out.println(this.Algorithm);
                System.out.println(moves);
                System.out.println("Num: " + Board.boardCount);
                System.out.println("Cost: " + goal.getCost());
                System.out.println(timeS + " seconds");
                //***************************************************************
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}