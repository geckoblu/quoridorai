package martijn.quoridor.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import martijn.quoridor.Config;
import martijn.quoridor.Core;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.PointOfView;
import martijn.quoridor.model.Position;
import martijn.quoridor.model.PutWall;
import martijn.quoridor.model.Wall;

/**
 * A JPanel that draws a Quoridor state and a shadow move.
 */
@SuppressWarnings("serial")
public class BoardCanvas extends JPanel implements GameListener {

    private static final double CELL_SIZE = 10;
    private static final float WALL_THICKNESS = 2;
    private static final double PLAYER_RADIUS = CELL_SIZE - WALL_THICKNESS - 2;

    private static final Color BACKGROUND_COLOR = Color.decode("#B0A092");
    private static final Color CELL_COLOR = Color.decode("#A18E80"); //"#554b44");
    private static final Color COORDINATES_COLOR = Color.decode("#554b44");
    private static final Color WALL_COLOR = Color.decode("#3E1E0F"); //Color.decode("#554b44");


    private double _scale;
    private double _boardWidth;
    private double _boardHeight;
    private double _cellSize;

    private PointOfView _pointOfView = PointOfView.POV1;

    /** The board this canvas visualizes. */
    private GameModel _gameModel;

    /** The current shadow move. */
    private Move _shadow;

    /**
     * Creates a new QuoridorCanvas.
     * */
    public BoardCanvas(GameModel gameModel) {
        _gameModel = gameModel;
        _gameModel.addGameListener(this);
    }

    /**
     * Converts from board coordinates to pixel coordinates.
     */
    private double getX(int bx) {
        double dim = Config.showCoordinates() ? 1 : 0.5;

        double delta = (getWidth() - _boardWidth) / 2;
        double x = (bx + dim) * _cellSize;

        if (_pointOfView == PointOfView.POV2) {
            x = _boardWidth - _cellSize - x;
        }

        x = x + delta;

        return x;

    }

    /**
     * Converts from board coordinates to pixel coordinates.
     */
    private double getY(int by) {
        double dim = Config.showCoordinates() ? 1 : 0.5;

        double delta = (getHeight() - _boardHeight) / 2;
        double y = ((by + dim) * _cellSize);

        if (_pointOfView == PointOfView.POV1) {
            y = _boardHeight - y - _cellSize;
        }

        y = y + delta;

        return y;
    }

    /**
     * Converts from pixel coordinates to board coordinates.
     * Returns {@code null} if something went wrong.
     */
    public Point2D toBoardCoordinates(Point mouseLocation) {
        if (mouseLocation == null) {
            return null;
        }

        double dim = Config.showCoordinates() ? 1 : 0.5;

        double deltaX = (getWidth() - _boardWidth) / 2;
        double x1 = (mouseLocation.x - deltaX) / _cellSize - dim;

        if (_pointOfView == PointOfView.POV2) {
            x1 = Board.SIZE - x1;
        }

        double deltaY = (getHeight() - _boardHeight) / 2;
        double y1;
        if (_pointOfView == PointOfView.POV1) {
            y1 = (_boardHeight + deltaY - mouseLocation.y) / _cellSize - dim;
        } else {
            y1 = (mouseLocation.y - deltaY) / _cellSize - dim;
        }


        Point2D.Double p = new Point2D.Double(x1, y1);
        return p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int dim = Config.showCoordinates() ? 2 : 1;

        double bWidth = (Board.SIZE + dim) * CELL_SIZE;
        double bHeight = (Board.SIZE + dim) * CELL_SIZE;
        _scale = Math.min(getWidth() / bWidth, getHeight() / bHeight);
        _scale *= .95;

        _cellSize = CELL_SIZE * _scale;
        _boardWidth = bWidth * _scale;
        _boardHeight = bHeight * _scale;

        g2.setColor(BACKGROUND_COLOR);
        double bx = (getWidth() - _boardWidth) / 2;
        double by = (getHeight() - _boardHeight) / 2;
        g2.fill(new RoundRectangle2D.Double(bx, by, _boardWidth, _boardHeight, 50.0, 50.0));
        drawCells(g2);

        drawCoordinates(g2);

        Board board = _gameModel.getBoard();

        // Draw walls.
        for (int x = 0; x < Board.SIZE - 1; x++) {
            for (int y = 0; y < Board.SIZE - 1; y++) {
                Position pos = new Position(x, y);
                Wall wall = board.getWall(pos);
                if (wall != null) {
                    drawWall(g2, wall, pos, false);
                }
            }
        }

        // Draw players.
        for (Player p : board.getPlayers()) {
            // System.out.println("BC " + p + " " + p.getPosition());
            drawPlayer(g2, p, p.getPosition(), false);
        }

        // Draw shadow.
        if (isShadowLegal()) {
            if (_shadow instanceof PutWall) {
                PutWall pw = (PutWall) _shadow;
                drawWall(g2, pw.getWall(), pw.getPosition(), true);
            } else if (_shadow instanceof Jump) {
                Jump j = (Jump) _shadow;
                drawPlayer(g2, board.getTurn(), j.getPosition(), true);
            }
        }
    }

    /** Draw a single cell **/
    private void drawCell(Graphics2D g2, int bx, int by) {

        double border = WALL_THICKNESS * _scale;
        double x1 = getX(bx);
        double y1 = getY(by);

        g2.setColor(CELL_COLOR);
        g2.fill(new Rectangle2D.Double(x1 + border / 2, y1 + border / 2, _cellSize - border, _cellSize - border));

    }

    /** Draw all the board cells **/
    private void drawCells(Graphics2D g2) {
        for (int bx = 0; bx < Board.SIZE; bx++) {
            for (int by = 0; by < Board.SIZE; by++) {
                drawCell(g2, bx, by);
            }
        }
    }

    /** Draw a single player. */
    private void drawPlayer(Graphics2D g2, Player p, Position pos, boolean shadow) {

        double radius = PLAYER_RADIUS * _scale;
        double delta = (_cellSize - radius) / 2;
        double x1 = getX(pos.getX()) + delta;
        double y1 = getY(pos.getY()) + delta;

        Shape s = new Ellipse2D.Double(x1, y1, radius, radius);

        // Draw fill.
        Color c = p.getColor();
        if (shadow) {
            c = Core.transparent(c);
        }
        g2.setColor(c);
        g2.fill(s);

        // Draw outline.
        c = Color.BLACK;
        if (shadow) {
            c = Core.transparent(c);
        }
        g2.setColor(c);
        g2.draw(s);
    }

    private void drawCoordinates(Graphics2D g2) {

        if (!Config.showCoordinates()) {
            return;
        }

        g2.setColor(COORDINATES_COLOR);

        FontMetrics metrics = g2.getFontMetrics();

        // Draw X coordinates
        for (int bx = 0; bx < Board.SIZE; bx++) {
            String coord;
            switch (Config.notation()) {
            case LAMEK:
            case GLENDENNING:
                coord = Character.toString((char) ('a' + bx));
                break;
            default:
                coord = Character.toString((char) ('0' + bx));
                break;
            }

            double deltaX = (_cellSize - metrics.stringWidth(coord)) / 2;
            double deltaY = _cellSize - (_cellSize - metrics.getHeight()) / 2;
            double x1 = getX(bx);

            double yAbove = getY(-1);
            g2.drawString(coord, (int) (x1 + deltaX), (int) (yAbove + deltaY));

            double yBelow = getY(Board.SIZE);
            g2.drawString(coord, (int) (x1 + deltaX), (int) (yBelow + deltaY));
        }

        // Draw Y coordinates
        for (int by = 0; by < Board.SIZE; by++) {
            String coord;
            switch (Config.notation()) {
            case LAMEK:
                coord = Character.toString((char) ('1' + by));
                break;
            case GLENDENNING:
                coord = Character.toString((char) ('9' - by));
                break;
            default:
                coord = Character.toString((char) ('0' + by));
                break;
            }

            double deltaX = (_cellSize - metrics.stringWidth(coord)) / 2;
            double deltaY = _cellSize - (_cellSize - metrics.getHeight()) / 2;
            double y1 = getY(by);

            double xLeft = getX(-1);
            g2.drawString(coord, (int) (xLeft + deltaX), (int) (y1 + deltaY));

            double xRight = getX(Board.SIZE);
            g2.drawString(coord, (int) (xRight + deltaX), (int) (y1 + deltaY));
        }
    }

    /** Draw a single wall. */
    private void drawWall(Graphics2D g2, Wall wall, Position pos, boolean shadow) {

        double x1;
        double y1;
        double border = WALL_THICKNESS * _scale / 2;

        Stroke oldStroke = g2.getStroke();
        BasicStroke newStroke = new BasicStroke((float) (border));
        g2.setStroke(newStroke);

        float lineWidth = newStroke.getLineWidth() / 2;
        double length = (_cellSize) * 2 - border - lineWidth;

        g2.setColor(shadow ? Core.transparent(WALL_COLOR, 0x8f) : WALL_COLOR);
        Shape line;

        switch (wall) {
        case HORIZONTAL:
            x1 = getX(pos.getX() + (_pointOfView == PointOfView.POV1 ? 0 : 1));
            y1 = getY(pos.getY() + (_pointOfView == PointOfView.POV1 ? 0 : 1));
            line = new Line2D.Double(x1 + border + lineWidth / 2, y1, x1 + length, y1);
            break;
        case VERTICAL:
            x1 = getX(pos.getX() + (_pointOfView == PointOfView.POV1 ? 1 : 0));
            y1 = getY(pos.getY() + (_pointOfView == PointOfView.POV1 ? 1 : 0));
            line = new Line2D.Double(x1, y1 + border + lineWidth / 2, x1, y1 + length);
            break;
        default:
            throw new InternalError();
        }

        g2.draw(line);

        g2.setStroke(oldStroke);
    }


    /** Returns the current shadow move. */
    public Move getShadow() {
        return _shadow;
    }

    /** Sets the current shadow move. */
    public void setShadow(Move shadow) {
        this._shadow = shadow;
        repaint();
    }

    /** Returns whether the shadow is non-null and legal. */
    public boolean isShadowLegal() {
        return _shadow != null && _shadow.isLegal(_gameModel.getBoard());
    }

    public void applyShadow() {
        _gameModel.getBoard().move(_shadow);
        setShadow(null);
    }

    public void setPointOfView(PointOfView pointOfView) {
        PointOfView oldPointOfView = _pointOfView;
        _pointOfView = pointOfView;
        repaint();
        firePropertyChange("POINT_OF_VIEW", oldPointOfView, _pointOfView);
    }

    public PointOfView getPointOfView() {
        return _pointOfView;
    }

    /**
     * BoardListener
     */
    @Override
    public void moveExecuted() {
        setShadow(null);
        repaint();
    }

    @Override
    public void newGame() {
        setShadow(null);
        repaint();
    }

}
