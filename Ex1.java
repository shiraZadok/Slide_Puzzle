import java.io.*;
import java.util.Scanner;

public class Ex1 {

    public static void main(String[] args) {
        Game game = new Game();
        try {
            Scanner input = new Scanner(new File("input.txt"));
            game.init(input);
            //Check if the board is solvable according to the position of the black square.
            if (game.board.isSolvable()) {
                switch (game.getAlgorithm()) {
                    case "BFS":
                        Algorithms.BFS(game);
                        break;
                    case "DFID":
                        Algorithms.DFID(game);
                        break;
                    case "A*":
                        Algorithms.AStar(game);
                        break;
                    case "IDA*":
                        Algorithms.IDAStar(game);
                        break;
                    case "DFBnB":
                        Algorithms.DFBnB(game);
                        break;
                }
            }
            else {
                long duration = 0;
                game.makeOutput(null, duration);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}