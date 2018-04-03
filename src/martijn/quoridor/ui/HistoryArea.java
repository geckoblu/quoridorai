package martijn.quoridor.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import martijn.quoridor.Core;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.Move;

@SuppressWarnings("serial")
public class HistoryArea extends JTextArea implements GameListener {

    private GameModel _gameModel;

    private DefaultHighlighter.DefaultHighlightPainter _painter;

    public HistoryArea(GameModel gameModel) {
        _gameModel = gameModel;
        _gameModel.addGameListener(this);

        _painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(232, 242, 254));

        DefaultHighlighter highlighter = (DefaultHighlighter) this.getHighlighter();
        highlighter.setDrawsLayeredHighlights(false);

        setFont(new Font(Font.MONOSPACED, Font.PLAIN, getFont().getSize()));
    }

    private void update() {

        StringBuffer sb = new StringBuffer();

        Iterator<Move> history = _gameModel.getHistory();
        int i = 0;
        while (history.hasNext()) {
            i++;

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

            sb.append("\n");
        }
        setText(sb.toString());

        if (_gameModel.hasHistory()) {
            int historyIndex = _gameModel.getHistoryIndex();
            if (historyIndex > 0) {
                int line = (historyIndex - 1) / 2;
                try {
                    int start = this.getLineStartOffset(line);
                    int end = this.getLineEndOffset(line);
                    this.getHighlighter().addHighlight(start, end, _painter);
                } catch (BadLocationException ex) {
                    Core.LOGGER.log(Level.WARNING, "Some exception occurs", ex);
                }
            }
        }
    }

    @Override
    // BoardListener
    public void moveExecuted() {
        update();
    }

    @Override
    // BoardListener
    public void newGame() {
        update();
    }

}
