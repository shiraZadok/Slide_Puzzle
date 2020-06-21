//
// Created by shira on 01/06/2020.
//
enum COLOR { R, G, B };

public class Square {
    //The num of the square
    private int num;
    //The color of the square
    private COLOR color;
    //The cost to move this square
    private int cost;

    //Default Constructors
    public Square() {
        this.num = 0;
        this.color = null;
        this.cost = 0;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public COLOR getColor() {
        return color;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }
}