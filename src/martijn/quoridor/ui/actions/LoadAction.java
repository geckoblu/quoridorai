package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Notation;
import martijn.quoridor.model.PutWall;

@SuppressWarnings("serial")
public class LoadAction extends AbstractAction {

    private final Component _parent;
    private Board _board;
    Notation notation;

    public LoadAction(Component parent, Board board) {
        super();

        I18N.Action action = I18N.getAction("LOAD");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
        putValue(Action.SHORT_DESCRIPTION, action.short_description);

        _parent = parent;
        _board = board;

        actionPerformed(null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Load");

        _board.newGame();

        boolean notationFound = false;
        try {
            Path path = Paths.get("/home/alessio/tmp/quoridor-test1.txt");

            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                System.out.println(line);
            }

            if (lines.size() < 2) {
                throw new LoadException("File too short");
            }

            Iterator<String> iter = lines.iterator();

            String first = iter.next();
            if (!first.equals("# QGF V[1.0]")) {
                throw new LoadException("Not a valid version");
            }

            int linenumber = 1;
            int movenumber = 0;
            while (iter.hasNext()) {
                String line = iter.next();
                linenumber++;

                if (line.startsWith("# Notation:")) {
                    notationFound = true;
                    notation = Notation.parse(line.substring(line.indexOf(":") + 1).trim());
                }

                int idx = line.indexOf(".");
                if (idx > -1) {
                    String pref = line.substring(0, idx).trim();

                    try {
                        int mv = Integer.parseInt(pref);
                        movenumber++;

                        if (mv == 1 && !notationFound) {
                            throw new LoadException("Notation not found");
                        }

                        if (mv != movenumber) {
                            String message = "Error on line " + linenumber + ": not a valid move number "
                                    + pref + " expected number " + movenumber;
                            throw new LoadException(message);
                        }

                        String moves = line.substring(idx + 1).trim();
                        parseMoves(movenumber, moves);

                    } catch (NumberFormatException nfex) {
                        String message = "Error on line " + linenumber + ": not a valid move number " + pref
                                + " not an integer";
                        throw new LoadException(message, nfex);
                    }
                }
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private void parseMoves(int movenumber, String moves) throws LoadException {
        String m1 = "";
        String m2 = "";
        int idx = moves.indexOf(" ");

        if (idx <= -1) {
            m1 = moves;
        } else {
            if (idx >= 2) {
                m1 = moves.substring(0, idx).trim();
                m2 = moves.substring(idx).trim();
            } else {
                String message = "Error on move " + movenumber + ": not a valid move " + moves;
                throw new LoadException(message);
            }
        }

        parseMove(movenumber, m1);
        parseMove(movenumber, m2);

    }

    private void parseMove(int movenumber, String move) throws LoadException {
        if (move == "") {
            return;
        }

        if (move.length() == 2) {
            Jump jump = new Jump(notation, move);
            _board.add(jump);

        } else if (move.length() == 3) {
            PutWall putwall = new PutWall(notation, move);
            _board.add(putwall);
        } else {

            String message = "Error on move " + movenumber + ": not a valid move " + move;
            throw new LoadException(message);
        }

    }

}

@SuppressWarnings("serial")
class LoadException extends IOException {

    LoadException(String message) {
        super(message);
    }

    public LoadException(Throwable cause) {
        super(cause);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
