package martijn.quoridor.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.Icon;
import javax.swing.JLabel;

import martijn.quoridor.Core;
import martijn.quoridor.anim.Animation;
import martijn.quoridor.anim.Animator;
import martijn.quoridor.anim.PlayJob;
import martijn.quoridor.model.Player;

@SuppressWarnings("serial")
public class PlayerIcon extends JLabel implements Icon {

    private static final int NFRAMES = 12;

    public static final int FLIP_DURATION = 500;

    private static final long BRAIN_DELAY = 2000;

    private Color color;

    // Animations.

    private Animator animator;

    private double scale = 1;

    private int alpha = 0x7f;

    public PlayerIcon(Color color) {

        this.color = color;

        animator = new Animator();
        setIcon(this);
    }

    public PlayerIcon(Player player) {
        this(player.getColor());
    }

    public void setPlayer(Player player) {
        this.color = player.getColor();
        repaint();
    }

    // Icon implementation.

    @Override
    public int getIconHeight() {
        return 16;
    }

    @Override
    public int getIconWidth() {
        return 16;
    }

    @Override
    public void paintIcon(Component comp, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getIconWidth() - 2;
        int h = getIconHeight() - 2;
        Shape disc = new Ellipse2D.Double(-w / 2.0, -h / 2.0, w, h);

        AffineTransform at = new AffineTransform();
        at.translate(x + 1 + w / 2.0, y + 1 + h / 2.0);
        at.scale(scale, 1);
        disc = at.createTransformedShape(disc);

        Color fill = Core.transparent(color, 0xff);
        Color stroke = Core.transparent(Color.BLACK, 0xff);

        g2.setColor(fill);
        g2.fill(disc);
        g2.setColor(stroke);
        g2.draw(disc);
    }

    public void update() {
        repaint();
    }

    // Animation.

    public void flipOnce() {
        animator.play(PlayJob.playOnce(new Flip(), true));
    }

    public void startFlippingContinuously() {
        animator.play(PlayJob.loop(new Flip(), true));
    }

    public void startFlippingSlowly() {
        animator.play(new PlayJob(new Flip(), PlayJob.LOOP, BRAIN_DELAY, BRAIN_DELAY, true));
    }

    public void stopFlipping() {
        synchronized (animator) {
            PlayJob current = animator.getCurrent();
            if (current != null && current.getAnimation() instanceof Flip) {
                animator.cancelCurrent();
            }
        }
    }

    private void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    private void setAlpha(int alpha) {
        this.alpha = alpha;
        repaint();
    }

    private class Flip implements Animation {

        @Override
        public int getFrameCount() {
            return NFRAMES;
        }

        @Override
        public long getFrameDisplayTime(int frame) {
            return FLIP_DURATION / NFRAMES;
        }

        @Override
        public void showFrame(int frame) {
            setScale(Math.cos((double) frame / (NFRAMES - 1) * Math.PI));
        }

        @Override
        public void animationStopped() {
            setScale(1);
        }

    }

//    private class FadeIn implements Animation {
//
//        @Override
//        public int getFrameCount() {
//            return NFRAMES;
//        }
//
//        @Override
//        public long getFrameDisplayTime(int frame) {
//            return 250 / NFRAMES;
//        }
//
//        @Override
//        public void showFrame(int frame) {
//            setAlpha(0x80 + 0x7f * frame / (NFRAMES - 1));
//        }
//
//        @Override
//        public void animationStopped() {
//            setAlpha(0x7f);
//        }
//
//    }

}
