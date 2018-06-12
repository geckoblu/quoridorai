package martijn.quoridor.anim;

public class PlayJob {

    public static final int LOOP = -1;

    /** The animation to play. */
    private Animation _animation;

    /** The number of times the animation should be played. */
    private int _times;

    /**
     * The number of milliseconds that should pass before the animation is
     * played for the first time.
     */
    private long _delayBefore;

    /**
     * The number of milliseconds that should pass between the various
     * animations.
     */
    private long _delayBetween;

    /**
     * Whether the individual animations should be played forwards or backwards.
     */
    private boolean _forward;

    /** Creates a new play job. */
    public PlayJob(Animation animation, int times, long delayBefore, long delayBetween, boolean forward) {
        if (animation == null) {
            throw new NullPointerException("Animation is null.");
        }
        this._animation = animation;
        this._times = times;
        this._delayBefore = delayBefore;
        this._delayBetween = delayBetween;
        this._forward = forward;
    }

    /** Creates a play job plays the animation once. */
    public static PlayJob playOnce(Animation animation, boolean forward) {
        return new PlayJob(animation, 1, 0, 0, forward);
    }

    /**
     * Creates a play job that continuously loops the animation, with no delays.
     */
    public static PlayJob loop(Animation animation, boolean forward) {
        return new PlayJob(animation, LOOP, 0, 0, forward);
    }

    public Animation getAnimation() {
        return _animation;
    }

    public long getDelayBefore() {
        return _delayBefore;
    }

    public long getDelayBetween() {
        return _delayBetween;
    }

    public boolean isForward() {
        return _forward;
    }

    public int getTimes() {
        return _times;
    }

    public boolean loops() {
        return _times == LOOP;
    }

    /**
     * Executes the play job in the current thread. Interrupting the thread will
     * cause an {@code InterruptedException} to be thrown as soon as it exits
     * any {@link Animation} method it might be in. If the animation is looping,
     * interrupting the thread is the only way to stop it.
     */
    public void execute() throws InterruptedException {
        for (int i = 0; i < _times || loops(); i++) {
            Thread.sleep(i == 0 ? _delayBefore : _delayBetween);
            playOnce(_forward);
        }
        _animation.animationStopped();
    }

    /**
     * Plays the animation once.
     *
     * @param forward
     *            whether the animation should be played forward.
     */
    private void playOnce(boolean forward) throws InterruptedException {
        int n = _animation.getFrameCount();
        for (int i = 0; i < n; i++) {
            int frame = forward ? i : n - i - 1;
            _animation.showFrame(frame);
            if (Thread.interrupted()) {
                throw new InterruptedException("Play job interrupted.");
            }
            Thread.sleep(_animation.getFrameDisplayTime(frame));
        }
    }

}
