import java.util.Comparator;

public class BoardComparator implements Comparator<Board>{

    @Override
    public int compare(Board board1, Board board2) {
        if (Algorithms.AStar_F(board1) > Algorithms.AStar_F(board2)) return 1;
        else if (Algorithms.AStar_F(board1) < Algorithms.AStar_F(board2)) return -1;
        else if (Algorithms.AStar_F(board1) == Algorithms.AStar_F(board2)) {
            if (board1.getProductNum() > board2.getProductNum()) return 1;
            else return -1;
        }
        return 0;
    }
}
