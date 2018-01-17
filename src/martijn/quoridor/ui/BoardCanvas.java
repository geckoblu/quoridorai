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

import javax.swing.JPanel;

import martijn.quoridor.Config;
import martijn.quoridor.Core;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.Position;
import martijn.quoridor.model.PutWall;
import martijn.quoridor.model.Wall;

/**
 * A JPanel that draws a Quoridor state and a shadow move.
 */
@SuppressWarnings("serial")
public class BoardCanvas extends JPanel implements BoardListener {

	private static final double CELL_SIZE = 10;
	private static final float WALL_THICKNESS = 2;
	private static final double PLAYER_RADIUS = CELL_SIZE - WALL_THICKNESS - 2;

	private double scale;
	private double boardWidth;
	private double boardHeight;
	private double cell_size;

	/** The board this canvas visualizes. */
	private Board board;

	/** The current shadow move. */
	private Move shadow;

	/** Creates a new QuoridorCanvas. */
	public BoardCanvas(Board board) {
		this.board = board;
		board.addBoardListener(this);
		//setBackground(Color.GREEN);
	}

	private double getX(int x) {
		int dim = Config.showCoordinates() ? 1 : 0;

		double delta = (getWidth() - boardWidth) / 2;
		return (x + dim) * cell_size + delta;
	}

	private double getY(int y) {
		int dim = Config.showCoordinates() ? 1 : 0;

		double delta = (getHeight() - boardHeight) / 2;
		return boardHeight - ((y + 1 + dim) * cell_size ) + delta;
	}

	/**
	 * Converts from pixel coordinates to board coordinates. Returns
	 * {@code null} if something went wrong.
	 */
	public Point2D toBoardCoordinates(Point ml) {
		if (ml == null) {
			return null;
		}

		int dim = Config.showCoordinates() ? 1 : 0;

		double delta = (getWidth() - boardWidth) / 2;
		double x1 = (ml.x - delta) / cell_size - dim;

		delta = (getHeight() - boardHeight) / 2;
		double y1 = (boardHeight + delta - ml.y) / cell_size - dim; // -1;

		Point2D.Double p = new Point2D.Double(x1, y1);
		return p;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(
		    RenderingHints.KEY_TEXT_ANTIALIASING,
		    RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g2.setRenderingHint(
		    RenderingHints.KEY_TEXT_ANTIALIASING,
		    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		int dim = Config.showCoordinates() ? 2 : 0;

		double bWidth = (board.getWidth() + dim) * CELL_SIZE;
		double bHeight = (board.getHeight() + dim) * CELL_SIZE;
		scale = Math.min(getWidth() / bWidth,
				         getHeight() / bHeight);
		scale *= .95;

		cell_size = CELL_SIZE * scale;
		boardWidth = bWidth * scale;
		boardHeight = bHeight * scale;

		//g2.setColor(Color.RED);
		//g2.draw(new Rectangle2D.Double((getWidth() - boardWidth) / 2, (getHeight() - boardHeight) / 2, boardWidth, boardHeight));

		drawCells(g2);

		drawCoordinates(g2);

		// Draw walls.
		for (int x = 0; x < board.getWidth() - 1; x++) {
			for (int y = 0; y < board.getHeight() - 1; y++) {
				Wall wall = board.getWall(new Position(x, y));
				if (wall != null) {
					drawWall(g2, wall, x, y, false);
				}
			}
		}

		// Draw players.
		for (Player p : board.getPlayers()) {
			//System.out.println("BC " + p + " " + p.getPosition());
			drawPlayer(g2, p, p.getPosition(), false);
		}

		// Draw shadow.
		if (isShadowLegal()) {
			if (shadow instanceof PutWall) {
				PutWall pw = (PutWall) shadow;
				drawWall(g2, pw.getWall(), pw.getPosition().getX(), pw.getPosition().getY(), true);
			} else if (shadow instanceof Jump) {
				Jump j = (Jump) shadow;
				drawPlayer(g2, board.getTurn(), j.getNewPosition(), true);
			}
		}
	}

	/** Draw a single cell **/
	private void drawCell(Graphics2D g2, int x, int y) {

		double border = WALL_THICKNESS * scale;
		double x1 = getX(x);
		double y1 = getY(y);

		g2.setColor(Color.GRAY);
		g2.draw(new Rectangle2D.Double(x1 + border / 2,
                y1 + border / 2,
                cell_size - border,
                cell_size - border));

	}

	/** Draw all the board cells **/
	private void drawCells(Graphics2D g2) {

		g2.setColor(Color.GRAY);
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				drawCell(g2, x, y);
			}
		}
	}

	/** Draw a single player. */
	private void drawPlayer(Graphics2D g2, Player p, Position pos, boolean shadow) {

		double radius = PLAYER_RADIUS * scale;
		double delta = (cell_size - radius) / 2;
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
		g2.setColor(Color.BLACK);
		g2.draw(s);
	}

	private void drawCoordinates(Graphics2D g2) {

		if (!Config.showCoordinates()) {
			return;
		}

		g2.setColor(Color.GRAY);

		FontMetrics metrics = g2.getFontMetrics();

		// Draw X coordinates
		for(int i = 0; i < board.getWidth(); i++) {
			String coord = Character.toString((char)('a' + i));

			double deltaX = (cell_size - metrics.stringWidth(coord)) / 2;
			double deltaY = cell_size - (cell_size - metrics.getHeight()) / 2;
			double x1 = getX(i);

			double y1 = getY(-1);
			g2.drawString(coord, (int)(x1 + deltaX), (int)(y1 + deltaY));

			y1 = getY(board.getHeight());
			g2.drawString(coord, (int)(x1 + deltaX), (int)(y1 + deltaY));
		}

		// Draw Y coordinates
		for(int i = 0; i < board.getWidth(); i++) {
			String coord;
			if (Config.lamekNotation()) {
				coord = Character.toString((char)('1' + i));
			} else {
				coord = Character.toString((char)('9' - i));
			}

			double deltaX = (cell_size - metrics.stringWidth(coord)) / 2;
			double deltaY = cell_size - (cell_size - metrics.getHeight()) / 2;
			double y1 = getY(i);

			double x1 = getX(-1);
			g2.drawString(coord, (int)(x1 + deltaX), (int)(y1 + deltaY));

			x1 = getX(board.getWidth());
			g2.drawString(coord, (int)(x1 + deltaX), (int)(y1 + deltaY));
		}
	}

	/** Draw a single wall. */
	private void drawWall(Graphics2D g2, Wall wall, int x, int y, boolean shadow) {

		double x1;
		double y1;
		double border = WALL_THICKNESS * scale / 2;

		Stroke oldStroke = g2.getStroke();
		BasicStroke newStroke = new BasicStroke((float)(border));
		g2.setStroke(newStroke);

		float lineWidth = newStroke.getLineWidth() / 2;
		double length = (cell_size ) * 2 - border - lineWidth;

		g2.setColor(shadow ? new Color(0x7f000000, true) : Color.BLACK);
		Shape line;

		switch (wall) {
			case HORIZONTAL:
				x1 = getX(x);
				y1 = getY(y);
				line = new Line2D.Double(x1 + border + lineWidth / 2, y1, x1 + length, y1);
				break;
			case VERTICAL:
				x1 = getX(x + 1);
				y1 = getY(y + 1);
				line = new Line2D.Double(x1, y1 + border + lineWidth / 2, x1, y1 + length);
				break;
			default:
				throw new InternalError();
		}

		g2.draw(line);

		g2.setStroke(oldStroke);
	}


	// Getters and setters.

	/** Returns the board this canvas visualizes. */
	public Board getBoard() {
		return board;
	}

	/** Returns the current shadow move. */
	public Move getShadow() {
		return shadow;
	}

	/** Sets the current shadow move. */
	public void setShadow(Move shadow) {
		this.shadow = shadow;
		repaint();
	}

	/** Returns whether the shadow is non-null and legal. */
	public boolean isShadowLegal() {
		return shadow != null && shadow.isLegal(getBoard());
	}

	public void applyShadow() {
		board.move(shadow);
		setShadow(null);
	}

	/**
	 * BoardListener
	 */
	@Override
	public void moveExecuted(Move move) {
		setShadow(null);
		repaint();
	}

	@Override
	public void movesUndone(Move[] moves) {
		setShadow(null);
		repaint();
	}

	@Override
	public void newGame() {
		setShadow(null);
		repaint();
	}

}
