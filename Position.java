//
// Created by shira on 01/06/2020.
//

public class Position {
    //The num of the row on the board
    int row;
    //The num of the column on the board
    int column;

    //Default Constructors
    public Position(){
        this.row = -1;
        this.column = -1;
    }

    //Copy Constructors
    public Position(Position pos){
        this.row = pos.row;
        this.column = pos.column;
    }

    public int getRow() { return row; }

    public int getColumn() {
        return column;
    }

    public void setPosition(int row, int column){
        this.row=row;
        this.column=column;
    }

    //Check - if two Position is same =true, else otherwise
    public boolean equals(Position pos){
        return this.row == pos.row && this.column==pos.column;
    }
}