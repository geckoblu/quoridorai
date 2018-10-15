package martijn.quoridor.brains;

import junit.framework.TestCase;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Utils;

public class TestDeepBrain extends TestCase {

    public void testDepth1() throws InterruptedException {
        // Board board = Utils.getBoard("20", "24", ""); // dim 5

        // Board board = Utils.getBoard("10", "12", ""); // dim 3
        // Board board = Utils.getBoard("01", "12", ""); // dim 3

        Board board = Utils.getBoard();

        DeepBrain deepbrain = new DeepBrain(5);

        // deepbrain.setDebug(false);
        deepbrain.setDeterministic(false);

        // Move bestmove = deepbrain.getMove(board);

        // System.out.println("BestMove >> " + bestmove);

    }

    public void testProfileDeep() throws InterruptedException {

        System.out.println("Profile Board size " + Board.SIZE);

//        profileDeep(1);
//
//        profileDeep(2);
//
//        profileDeep(3);

        profileDeep(4);

        profileDeep(5);
    }

    private void profileDeep(int deep) throws InterruptedException {
        System.out.println("");
        System.out.println("Profiling deep " + deep);

        Board board;
        DeepBrain deepbrain;

        System.out.println("  NOT SORTED");

        board = Utils.getBoard();
        deepbrain = new DeepBrain(deep);
        deepbrain.setDeterministic(true);
        deepbrain.setSortMoves(false);

        profileMove(deepbrain, board);

        System.out.println("  SORTED");

        board = Utils.getBoard();
        deepbrain = new DeepBrain(deep);
        deepbrain.setDeterministic(true);
        deepbrain.setSortMoves(true);

        profileMove(deepbrain, board);

    }

    private void profileMove(DeepBrain deepbrain, Board board) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Move bestmove = deepbrain.getMove(board);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        System.out.println("    Best move:     " + bestmove);
        System.out.println("    Counter:       " + deepbrain._counter);
        System.out.println("    Counter alpha: " + deepbrain._counterAlpha);
        System.out.println("    Time:          " + timeToString(elapsedTime));
    }

    private String timeToString(long elapsedTime) {
        if (elapsedTime < 1000) {
            return "" + elapsedTime + " ms";
        }

        elapsedTime = elapsedTime / 1000;

        if (elapsedTime < 60) {
            return "" + elapsedTime + " s";
        }

        elapsedTime = elapsedTime / 60;

        return "" + elapsedTime + " m";
    }

}
