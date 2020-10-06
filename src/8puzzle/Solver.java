import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Stack;

public class Solver
{
    private MinPQ<Node> BoardPQ, altBoardPQ;
    private Stack<Board> minSol;
    private Node goal;

    private class Node
    {
        Board board;
        Node prev;
        int manhattPriority, hammingPriority;
        int moves;
        Node(Board a, Node prev, int moves)
        {
            board = a;
            this.prev = prev;
            this.moves = moves;
            manhattPriority = board.manhattan() + this.moves;
            hammingPriority = board.manhattan() + this.moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if(initial == null) throw new IllegalArgumentException();

        minSol = new Stack<>();
        BoardPQ = new MinPQ<>( ManhattanFunction());
        altBoardPQ = new MinPQ<>( ManhattanFunction());

        BoardPQ.insert(new Node(initial, null, 0));
        altBoardPQ.insert(new Node(initial.twin(), null, 0));

        Node min = BoardPQ.delMin();
        Node twinMin = altBoardPQ.delMin();

        while(!min.board.isGoal() && !twinMin.board.isGoal()){

            for(Board b : min.board.neighbors()){
                if(min.prev == null || !b.equals(min.prev.board)){
                    BoardPQ.insert(new Node(b, min, min.moves+1));
                }
            }

            for(Board b : twinMin.board.neighbors()){
                if(twinMin.prev == null || !b.equals(twinMin.prev.board)){
                    altBoardPQ.insert(new Node(b, twinMin, twinMin.moves+1));
                }
            }
            min = BoardPQ.delMin();
            twinMin = altBoardPQ.delMin();
        }
        if(min.board.isGoal()) goal = min;
        else                   goal = null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return goal != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if(!isSolvable())
            return -1;
        else
            return goal.moves;
    }



    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        if (!isSolvable()) return null;
        Node curr = goal;

        while(curr != null){
            minSol.push(curr.board);
            curr = curr.prev;
        }

       return minSol;
    }


    // Define Manhattan and Hamming comparators for PQ
    private Comparator<Node> ManhattanFunction()
    { return new ManhattanPriority(); }

    private Comparator<Node> HammingFunction()
    { return new HammingPriority(); }

    private class ManhattanPriority implements Comparator<Node>{
        public int compare(Node a, Node b){
            if(a.manhattPriority < b.manhattPriority)
                return -1;
            else if(a.manhattPriority > b.manhattPriority)
                return 1;
            else
                return 0;
        }
    }

    private class HammingPriority implements Comparator<Node>{
        public int compare(Node a, Node b){
            if(a.hammingPriority < b.hammingPriority)
                return -1;
            else if(a.hammingPriority > b.hammingPriority)
                return 1;
            else
                return 0;
        }
    }


    // test client (see below)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
