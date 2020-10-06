import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board
{
    private final int board[][];
    private int dimension, hammDist, manhattDist;
    private ArrayList<Board> neighbours;
    private int emptyRow, emptyCol;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        board = tiles;
        dimension = tiles.length;
        hammDist = 0;
        manhattDist = 0;
        neighbours = new ArrayList<>();
        emptyRow = emptyCol = -1;

        for(int i = 0;  i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                // find location of empty spot in board
                if(tiles[i][j] == 0){
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString()
    {
        String result = Integer.toString(dimension) + "\n";
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                result += Integer.toString(board[i][j]) + " ";
            }
            result += "\n";
        }
        return result;
    }

    // board dimension n
    public int dimension() { return dimension; }

    // number of tiles out of place
    public int hamming() {
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(board[i][j] != 0 && board[i][j] != i*dimension + (j+1))
                    hammDist += 1;
            }
        }
        return hammDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(board[i][j] != 0 && board[i][j] != i*dimension + (j+1)){
                    int val = board[i][j] - 1;
                    int rowDist = Math.abs( val % dimension - j);
                    int colDist = Math.abs( val / dimension - i);
                    manhattDist = manhattDist + rowDist + colDist;
                }
            }
        }
        return manhattDist;
    }


    // is this board the goal board?
    public boolean isGoal() {
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[i][j] != 0 && board[i][j] != i*dimension + (j+1))
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if(y == null) return false;
        if(y == this) return true;

        if(y.getClass() != this.getClass()) return false;
        Board b = (Board) y;
        return Arrays.deepEquals(board, b.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        // get location of 'empty' cell

       /* int row = -1, col = -1;
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(board[i][j] == 0){
                    row = i;
                    col = j;
                }
            }
        }
        */
        // Check for all possible neighbours and add them
        // to neighbours



        if(validateIndex(emptyRow, emptyCol - 1)){
            int[][] tempNeigh = copy(board);
            swap(tempNeigh, emptyRow, emptyCol, emptyRow, emptyCol - 1);
            neighbours.add(new Board(tempNeigh));
        }
        if(validateIndex(emptyRow, emptyCol + 1)){
            int[][] tempNeigh = copy(board);
            swap(tempNeigh, emptyRow, emptyCol, emptyRow, emptyCol + 1);
            neighbours.add(new Board(tempNeigh));
        }
        if(validateIndex(emptyRow - 1, emptyCol)){
            int[][] tempNeigh = copy(board);
            swap(tempNeigh, emptyRow - 1, emptyCol, emptyRow, emptyCol);
            neighbours.add(new Board(tempNeigh));
        }
        if(validateIndex(emptyRow + 1, emptyCol)){
            int[][] tempNeigh = copy(board);
            swap(tempNeigh, emptyRow + 1, emptyCol, emptyRow, emptyCol);
            neighbours.add(new Board(tempNeigh));
        }

        return neighbours;
    }

    private void swap(int board[][], int rowOld, int colOld, int rowNew, int colNew){
        int temp = board[rowOld][colOld];
        board[rowOld][colOld] = board[rowNew][colNew];
        board[rowNew][colNew] = temp;
    }

    private boolean validateIndex(int row, int col){
        return (row >= 0 && row <= dimension - 1) && (col >= 0 && col <= dimension - 1);
    }

    private int[][] copy(int[][] a){
        int b[][] = new int[a.length][a.length];
        for(int i = 0; i < b.length; i++){
            for(int j = 0; j < b.length; j++){
                b[i][j] = a[i][j];
            }
        }
        return b;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        int twin[][] = copy(board);
        int row1 = emptyRow, col1 = emptyCol, row2 = emptyRow, col2 = emptyCol;

        // change row index of first pair
        while(twin[row1][col1] == 0)
            row1 = StdRandom.uniform(0, dimension);

        // change col index of second pair
        while(twin[row2][col2] == 0)
            col2 = StdRandom.uniform(0, dimension);

        // swap
        swap(twin, row1, col1, row2, col2);

        return new Board(twin);
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {
        int b[][] = {{1, 2, 3}, {4, 5, 0}, {7, 8, 6}};
        Board board = new Board(b);
        for(Board n : board.neighbors())
            System.out.println(n.toString());

        System.out.println("Hamming: " + board.hamming());
        System.out.println("Manhattan: " + board.manhattan());
        System.out.println("Twin: ");
        System.out.println(board.twin().toString());
    }

}
