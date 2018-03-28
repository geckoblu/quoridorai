package martijn.quoridor.anim;

/**
 * An animation. Upon creation, the animation should be in reset state.
 */
public interface Animation {

    /**
     * Returns the number of frames in the animation.
     */
    int getFrameCount();

    /**
     * Returns the number of milliseconds the specified frame should be visible
     * before the next is displayed.
     */
    long getFrameDisplayTime(int frame);

    /**
     * Causes the animation to show the next frame.
     */
    void showFrame(int frame);

    /**
     * Notification that the animation has stopped, either because there were no
     * more frames to show, or because the animation was cancelled.
     */
    void animationStopped();

}
