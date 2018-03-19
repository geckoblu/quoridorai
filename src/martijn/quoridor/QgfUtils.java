package martijn.quoridor;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Notation;
import martijn.quoridor.model.PutWall;

public final class QgfUtils {

    private static final String NL = System.getProperty("line.separator");

    Component _parent;
    Path _path;
    Notation _notation;
    List<Move> _moves;

    BufferedWriter _w;

    public QgfUtils(Component parent) {
        _parent = parent;
    }

    public Iterator<Move> load() {
        return load(null);
    }

    public Iterator<Move> load(Path fileToLoad) {

        if (fileToLoad == null) {

            File lastLoadPath;
            Path lastLoadFile = Config.lastLoadFile();
            if (lastLoadFile != null) {
                lastLoadPath = lastLoadFile.toFile();
            } else {
                lastLoadPath = new File(Config.lastLoadPath());
            }

            JFileChooser chooser = new JFileChooser(lastLoadPath);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("QGF Files", "txt");
            chooser.setFileFilter(filter);
            if (lastLoadFile != null) {
                chooser.setSelectedFile(lastLoadFile.toFile());
            }

            int returnVal = chooser.showOpenDialog(_parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileToLoad = chooser.getSelectedFile().toPath();
                Config.lastLoadFile(fileToLoad);
            } else {
                return null;
            }
        }

        try {
            parseQgf(fileToLoad);
        } catch (NoSuchFileException e1) {
            String message = I18N.tr("NO_SUCH_FILE") + ":\n" + e1.getFile();
            JOptionPane.showMessageDialog(_parent, message, "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (LoadException e1) {
            String message = I18N.tr("ERROR_PARSING") + ": " + _path.getFileName() + "\n" + e1.getMessage();
            JOptionPane.showMessageDialog(_parent, message, "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(_parent, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return _moves.iterator();

    }

    private void parseQgf(Path path) throws IOException {

        _path = path;
        _moves = new ArrayList<Move>();

        List<String> lines = Files.readAllLines(path);

        if (lines.size() < 2) {
            throw new LoadException("File too short");
        }

        Iterator<String> iter = lines.iterator();

        String first = iter.next();
        if (!first.equals("# QGF V[1.0]")) {
            throw new LoadException("Not a valid version");
        }

        boolean notationFound = false;

        int linenumber = 1;
        int movenumber = 0;
        while (iter.hasNext()) {
            String line = iter.next();
            linenumber++;

            if (line.startsWith("# Notation:")) {
                notationFound = true;
                _notation = Notation.parse(line.substring(line.indexOf(":") + 1).trim());
            }

            int idx = line.indexOf(".");
            if (idx > -1) {
                String pref = line.substring(0, idx).trim();

                try {
                    int mv = Integer.parseInt(pref);
                    movenumber++;

                    if (mv >= 1 && !notationFound) {
                        throw new LoadException("Notation not found");
                    }

                    if (mv != movenumber) {
                        String message = "Error on line " + linenumber + ": not a valid move number " + pref
                                + ", expected number " + movenumber;
                        throw new LoadException(message);
                    }

                    String moves = line.substring(idx + 1).trim();
                    parseMoves(movenumber, moves);

                } catch (NumberFormatException nfex) {
                    String message = "Error on line " + linenumber + ": not a valid move number " + pref
                            + ", not an integer";
                    throw new LoadException(message, nfex);
                }
            }
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
            Jump jump = new Jump(_notation, move);
            _moves.add(jump);

        } else if (move.length() == 3) {
            PutWall putwall = new PutWall(_notation, move);
            _moves.add(putwall);
        } else {

            String message = "Error on move " + movenumber + ": not a valid move " + move;
            throw new LoadException(message);
        }

    }

    public void save(Iterator<Move> moves) {
        save(null, moves);
    }

    private void save(Path fileToSave, Iterator<Move> moves) {

        if (fileToSave == null) {

            File lastLoadPath;
            Path lastLoadFile = Config.lastLoadFile();
            if (lastLoadFile != null) {
                lastLoadPath = lastLoadFile.toFile();
            } else {
                lastLoadPath = new File(Config.lastLoadPath());
            }

            JFileChooser chooser = new JFileChooser(lastLoadPath);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("QGF Files", "txt");
            chooser.setFileFilter(filter);
            if (lastLoadFile != null) {
                chooser.setSelectedFile(lastLoadFile.toFile());
            }

            int returnVal = chooser.showSaveDialog(_parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileToSave = chooser.getSelectedFile().toPath();
                Config.lastLoadFile(fileToSave);
            } else {
                return;
            }
        }

        try {
            saveQgf(fileToSave, moves);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(_parent, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void saveQgf(Path fileToSave, Iterator<Move> moves) throws IOException {

        try {
            _w = new BufferedWriter(new FileWriter(fileToSave.toFile()));

            writenl("# QGF V[1.0]");

            writenl("# Player1: ");
            writenl("# Player2: ");

            writenl("# Notation: " + Config.notation());

            int i = 0;
            while (moves.hasNext()) {
                i++;

                StringBuffer sb = new StringBuffer();
                String line = "  " + i + ".  ";
                line = line.substring(line.length() - 6);
                sb.append(line);

                Move m1 = moves.next();
                String m1s = m1.notation() + "   ";
                m1s = m1s.substring(0, 5);
                sb.append(m1s);

                if (moves.hasNext()) {
                    Move m2 = moves.next();
                    sb.append(" " + m2.notation());
                }

                writenl(sb.toString());
            }

        } finally {
            _w.close();
        }
    }

    /*
    private void write(String str) throws IOException {
        _w.write(str);
    }
    */

    private void writenl(String str) throws IOException {
        _w.write(str);
        _w.write(NL);
    }

    @SuppressWarnings("serial")
    private class LoadException extends IOException {

        LoadException(String message) {
            super(message);
        }

        /*
        LoadException(Throwable cause) {
            super(cause);
        }
        */

        LoadException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
