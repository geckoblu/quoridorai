package martijn.quoridor.brains;

import junit.framework.TestCase;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Utils;

public class TestSmartBrain extends TestCase {

    public void testDepth1() throws InterruptedException {
        Board board = Utils.getBoard("43", "45", "");

        System.out.println(board.hashCode());

        SmartBrain smartbrain = new SmartBrain(6);

        smartbrain.setDebug(false);
        smartbrain.setDeterministic(false);

        Move bestmove = smartbrain.getMove(board);

        System.out.println("BestMove >> " + bestmove);

    }

}
