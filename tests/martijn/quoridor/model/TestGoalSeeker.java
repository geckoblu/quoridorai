package martijn.quoridor.model;

import org.junit.Assert;

import junit.framework.TestCase;

public class TestGoalSeeker extends TestCase {

    /**
     * Easy situation: an empty board, each player are on their start position.
     */
    public void testEasyPath1() {

        Orientation[] targetPath0 = Utils.getOrientationArray("N N N N N N N N");
        Orientation[] targetPath1 = Utils.getOrientationArray("S S S S S S S S");

        Board board = new Board();

        GoalSeeker gs0 = new GoalSeeker(board.getPlayer(0));
        Orientation[] path0 = gs0.getPath();

        Assert.assertArrayEquals(targetPath0, path0);

        //System.out.println(Arrays.deepToString(path1));

        GoalSeeker gs1 = new GoalSeeker(board.getPlayer(1));
        Orientation[] path1 = gs1.getPath();

        //System.out.println(Arrays.deepToString(path2));

        Assert.assertArrayEquals(targetPath1, path1);

    }

    /**
     * Easy situation: an empty board, player1 at his start position, player0 one step .
     */
    public void testEasyPath2() {

        Orientation[] targetPath0 = Utils.getOrientationArray("N N N N N N N");
        Orientation[] targetPath1 = Utils.getOrientationArray("S S S S S S S S");

        Board board = Utils.getBoard("e2", "e9", "");

        GoalSeeker gs0 = new GoalSeeker(board.getPlayer(0));
        Orientation[] path0 = gs0.getPath();

        Assert.assertArrayEquals(targetPath0, path0);

        //System.out.println(Arrays.deepToString(path1));

        GoalSeeker gs1 = new GoalSeeker(board.getPlayer(1));
        Orientation[] path1 = gs1.getPath();

        //System.out.println(Arrays.deepToString(path2));

        Assert.assertArrayEquals(targetPath1, path1);

    }
}