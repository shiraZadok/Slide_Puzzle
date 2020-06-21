//
// Created by shira on 01/06/2020.
//
import java.util.*;
import java.util.stream.LongStream;

public class Algorithms {
    //Represent the number of the iteration in print
    public static int Iteration=0;

    /**
     *Algorithm of BFS
     * @param game continue the start board
     */
    public static void BFS(Game game) {
        long startTime = System.currentTimeMillis();
        long duration;
        Queue<Board> qFrontier = new LinkedList<>();
        Hashtable<String, Board> hFrontier = new Hashtable<>();
        qFrontier.add(game.board);
        hFrontier.put(game.board.getKey(), game.board);
        Hashtable<String, Board> ExploredSet = new Hashtable<>();
        while (!qFrontier.isEmpty()) {
            if (game.getOpenList()) print(hFrontier);
            Board out = qFrontier.remove();
            hFrontier.remove(out.getKey());
            ExploredSet.put(out.getKey(), out);
            for (MoveDIR move : MoveDIR.values()) {
                Board newState = out.Operator(move);
                if (newState != null) {
                    if (!hFrontier.containsKey(newState.getKey()) && !ExploredSet.containsKey(newState.getKey())) {
                        if (isSolved(newState)) {
                            duration = System.currentTimeMillis() - startTime;
                            game.makeOutput(newState, duration);
                            return;
                        }
                        qFrontier.add(newState);
                        hFrontier.put(newState.getKey(), newState);
                    }
                }
            }
        }
        duration = System.currentTimeMillis() - startTime;
        game.makeOutput(null, duration);
    } //END BFS

    /**
     *Algorithm of DFID
     * @param game continue the start board
     */
    public static void DFID(Game game) {
        long startTime = System.currentTimeMillis();
        for (int depth =1; depth<Integer.MAX_VALUE; depth++){
            Hashtable<String, Board> h_exist = new Hashtable<>();
            Game result = Limited_DFS(game, depth, h_exist);
            //If result == null - Limited_DFS fail or succeed, else is cut off - continue in the loop
            if (result!= null){
                long duration = System.currentTimeMillis() - startTime;
                result.makeOutput(result.board, duration);
                return;
            }
        }
    } //END DFID

    private static Game Limited_DFS(Game game, int limit, Hashtable<String,Board> h_exist) {
        if (isSolved(game.board)) { //game.setGameOver(true);
            return game; }
        //If the game not over its will stay false
        else if (limit == 0) { return null;}
        else {
            h_exist.put(game.board.getKey(), game.board);
            boolean isCutOff = false;
            for (MoveDIR move : MoveDIR.values()){
                Board newState = game.board.Operator(move);
                if (newState!=null) {
                    if (!h_exist.containsKey(newState.getKey())) {
                        Game newGame = new Game(game, newState);
                        Game result = Limited_DFS(newGame, limit - 1, h_exist);
                        //If the game == null its cut off
                        if (result==null) isCutOff = true;
                        //If the bord != null its not fail
                        else if (result.board != null) { return result; }
                    }
                }
            }
            if (game.getOpenList()) print(h_exist);
            h_exist.remove(game.board.getKey());
            //Is cut off
            if (isCutOff) return null;
            //Is fail, game.board = true
            else { game.board = null; return game;}
        }
    } //END LIMITED_DFS

    /**
     *Algorithm of A*
     * @param game continue the start board
     */
    public static void AStar(Game game){
        long startTime = System.currentTimeMillis();
        long duration;
        PriorityQueue<Board> qFrontier = new PriorityQueue<>(new BoardComparator());
        Hashtable<String, Board> hFrontier = new Hashtable<>();
        qFrontier.add(game.board);
        hFrontier.put(game.board.getKey(),game.board);
        Hashtable<String, Board> ExploredSet = new Hashtable<>();
        while (!qFrontier.isEmpty()) {
            if (game.getOpenList()) print(hFrontier);
            Board out = qFrontier.poll();
            hFrontier.remove(out.getKey());
            if (isSolved(out)){
                duration = System.currentTimeMillis() - startTime;
                game.makeOutput(out, duration);
                return;
            }
            ExploredSet.put(out.getKey(), out);
            for (MoveDIR move : MoveDIR.values()) {
                Board newState = out.Operator(move);
                if (newState != null) {
                    if (!hFrontier.containsKey(newState.getKey()) && !ExploredSet.containsKey(newState.getKey())) {
                        qFrontier.add(newState);
                        hFrontier.put(newState.getKey(), newState);
                    }
                    else if (hFrontier.get(newState.getKey())!=null && hFrontier.get(newState.getKey()).getCost()>newState.getCost()) {
                        qFrontier.remove(hFrontier.get(newState.getKey()));
                        qFrontier.add(newState);
                        hFrontier.replace(newState.getKey(), newState);
                    }
                }
            }
        }
        duration = System.currentTimeMillis() - startTime;
        game.makeOutput(null, duration);
    } //END A*

    /**
     * This method is the best estimate of a lowest cost path from the initial state to a goal state
     * that is constrained to pass through Board board.
     * @param board represent the specific state
     * @return the estimate
     */
    public static int AStar_F(Board board){
        return board.getCost() + Manhattan_Distance_Heuristic(board);
    } //END ASTAR_F

    /**
     *Algorithm of IDA*
     * @param game continue the start board
     */
    public static void IDAStar(Game game){
        long startTime = System.currentTimeMillis();
        long duration;
        Stack<Board> sFrontier = new Stack<>();
        Hashtable<String, Board> hFrontier = new Hashtable<>();
        sFrontier.push(game.board);
        hFrontier.put(game.board.getKey(),game.board);
        int threshold = Manhattan_Distance_Heuristic(game.board);
        while (threshold!=Integer.MAX_VALUE){
            int minF = Integer.MAX_VALUE;
            game.board.setMark(false);
            sFrontier.push(game.board);
            while (!sFrontier.empty()){
                if (game.getOpenList()) print(hFrontier);
                Board out = sFrontier.pop();
                hFrontier.remove(out.getKey());
                if (out.getMark()) hFrontier.remove(out.getKey());
                else{
                    out.setMark(true);
                    sFrontier.push(out);
                    for (MoveDIR move : MoveDIR.values()) {
                        Board newState = out.Operator(move);
                        if (newState != null) {
                            int f_newState = AStar_F(newState);
                            if (f_newState > threshold){
                                minF = Math.min(minF, f_newState);
                                continue;
                            }
                            if (hFrontier.containsKey(newState.getKey()) && hFrontier.get(newState.getKey()).getMark()) continue;
                            if (hFrontier.containsKey(newState.getKey()) && !hFrontier.get(newState.getKey()).getMark()){
                                if (AStar_F(hFrontier.get(newState.getKey())) > AStar_F(newState)){
                                    sFrontier.removeElement(hFrontier.get(newState.getKey()));
                                    hFrontier.remove(newState.getKey());
                                }
                                else continue;
                            }
                            if (isSolved(newState)) {
                                duration = System.currentTimeMillis() - startTime;
                                game.makeOutput(newState, duration);
                                return;
                            }
                            sFrontier.push(newState);
                            hFrontier.put(newState.getKey(),newState);
                        }
                    }
                }
            }
            threshold = minF;
        }
        duration = System.currentTimeMillis() - startTime;
        game.makeOutput(null, duration);
    } //END IDA*

    /**
     *Algorithm of DFBnB
     * @param game continue the start board
     */
    public static void DFBnB(Game game){
        long startTime = System.currentTimeMillis();
        long duration;
        Stack<Board> sFrontier = new Stack<>();
        Hashtable<String, Board> hFrontier = new Hashtable<>();
        sFrontier.push(game.board);
        hFrontier.put(game.board.getKey(),game.board);
        //initialization the threshold
        int numSquareWithoutBlack = game.board.getSizeOfBoard() - game.board.getBlack().size();
        long threshold =  numSquareWithoutBlack <=12 ?
                LongStream.rangeClosed(1, numSquareWithoutBlack).reduce(1, (a, b) -> a * b): Integer.MAX_VALUE;
        Board result = null;
        while (!sFrontier.empty()){
            if (game.getOpenList()) print(hFrontier);
            Board out = sFrontier.pop();
            if (out.getMark()) hFrontier.remove(out.getKey());
            else {
                out.setMark(true);
                sFrontier.push(out);
                PriorityQueue<Board> qCheck = new PriorityQueue<>(new BoardComparator());
                for (MoveDIR move : MoveDIR.values()) {
                    Board newState = out.Operator(move);
                    if (newState != null) qCheck.add(newState);
                }
                //Temp stack to insert in reverse
                Stack<Board> temp = new Stack<>();
                while (!qCheck.isEmpty()){
                    if (AStar_F(qCheck.peek()) >= threshold) qCheck.clear();
                    else if (hFrontier.containsKey(qCheck.peek().getKey()) && hFrontier.get(qCheck.peek().getKey()).getMark())
                        qCheck.poll();
                    else if (hFrontier.containsKey(qCheck.peek().getKey()) && !hFrontier.get(qCheck.peek().getKey()).getMark()){
                        if (AStar_F(hFrontier.get(qCheck.peek().getKey())) <= AStar_F(qCheck.peek())) qCheck.poll();
                        else {
                            sFrontier.removeElement(hFrontier.get(qCheck.peek().getKey()));
                            hFrontier.remove(qCheck.peek().getKey());
                        }
                    }
                    else if (isSolved(qCheck.peek())) {
                        threshold = AStar_F(qCheck.peek());
                        result = qCheck.poll();
                        qCheck.clear();
                    }
                    else temp.push(qCheck.poll());
                }
                while (!temp.empty()){ sFrontier.push(temp.peek()); hFrontier.put(temp.peek().getKey(), temp.pop()); }
                temp.clear();
            }
        }
        duration = System.currentTimeMillis() - startTime;
        game.makeOutput(result, duration);
    } //END DFBnB

    /**
     * This method print all the board that are in the hashtable
     * @param hFrontier - The hashtable that stored all the frontier vertex
     */
    private static void print(Hashtable<String,Board> hFrontier) {
        Iteration++;
        System.out.println("************** Iteration" +Iteration+ " **************");
        for (Board b: hFrontier.values()) {
            b.printBoard();
            System.out.println("----------------");
        }
    }

    /**
     * This method check if the board solved
     * @param board to check
     * @return true if solved, false otherwise
     */
    private static boolean isSolved(Board board) {
        for (int i = 0, check = 1; i<board.getRow(); i++) {
            for (int j = 0; j < board.getColumn(); check++, j++) {
                if (board.board[i][j].getNum() != check)
                    return false;
            }
        }
        return true;
    }

    /**
     *This method calculates manhattan distance between all pairs of coordinates
     * and multiplies the steps of moving in the cost-move of the square.
     * Note: The black square did not count because there cost is zero. They can't be moving.
     * @param board current board
     * @return the sum of manhattan distance and the multiplies
     */
    private static int Manhattan_Distance_Heuristic(Board board) {
        int sum = 0;
        for (int i = 0; i < board.getRow(); i++) {
            for (int j = 0; j < board.getColumn(); j++) {
                //we do not consider the blank square
                if (board.board[i][j].getNum() != board.getSizeOfBoard()) {
                    int g_row = (board.board[i][j].getNum() - 1) / board.getColumn();
                    int g_col = (board.board[i][j].getNum() - 1) % board.getColumn();
                    sum += (Math.abs(g_row - i) + Math.abs(g_col - j)) * board.board[i][j].getCost();
                }
            }
        }
        return sum;
    }
}