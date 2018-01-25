package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.Config;
import martijn.quoridor.I18N;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;

@SuppressWarnings("serial")
public class SaveAction extends AbstractAction implements BoardListener {

    private static final String NL = System.getProperty("line.separator");

    private final Component _parent;
    private Board _board;

    private BufferedWriter _w;

    public SaveAction(Component parent, Board board) {
        super();

        I18N.Action action = I18N.getAction("SAVE");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
        putValue(Action.SHORT_DESCRIPTION, action.short_description);

        _parent = parent;
        _board = board;

        update();

        _board.addBoardListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Save");

        try {
            try {
                _w = new BufferedWriter(new FileWriter("/home/alessio/tmp/quoridor-save.txt"));

                writenl("# QGF V[1.0]");

                writenl("# Player1: ");
                //board.getS
                writenl("# Player2: ");

                write("# Notation: " + Config.notation());

                Iterator<Move> history = _board.getHistory();
                int i = 0;
                while (history.hasNext()) {
                    i++;

                    StringBuffer sb = new StringBuffer();
                    String line = "  " + i + ".  ";
                    line = line.substring(line.length() - 6);
                    sb.append(line);

                    Move m1 = history.next();
                    String m1s = m1.notation() + "   ";
                    m1s = m1s.substring(0, 5);
                    sb.append(m1s);

                    if (history.hasNext()) {
                        Move m2 = history.next();
                        sb.append(" " + m2.notation());
                    }

                    writenl(sb.toString());
                }

            } finally {
                _w.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void moveExecuted() {
        update();
    }

    @Override
    public void newGame() {
        update();
    }

    private void update() {
        setEnabled(_board.hasHistory());
    }

    private void write(String str) throws IOException {
        _w.write(str);
    }

    private void writenl(String str) throws IOException {
        _w.write(str);
        _w.write(NL);
    }

}
