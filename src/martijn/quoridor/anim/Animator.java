package martijn.quoridor.anim;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import martijn.quoridor.Core;

/**
 * An animator manages one thread that executes play jobs.
 */
public class Animator implements Runnable {

    /** The thread that executes the play jobs. */
    private final Thread _thread;

    /** The queue containing the play jobs to be run. */
    private BlockingQueue<PlayJob> _queue;

    /** The currently running play job. */
    private PlayJob _current;

    /** Creates a new animator. */
    public Animator() {
        _queue = new LinkedBlockingQueue<PlayJob>();
        _thread = new Thread(this, toString());
        _thread.setDaemon(true);
        _thread.setPriority(Thread.MAX_PRIORITY);
        _thread.start();
    }

    /**
     * Returns the currently running play job, or {@code null} if the animator
     * is idle.
     */
    public synchronized PlayJob getCurrent() {
        return _current;
    }

    /** Cancels the currently running play job, if there is one. */
    public synchronized void cancelCurrent() {
        if (_current != null) {
            _thread.interrupt();
        }
    }

    /** Adds the play job to the queue to be run soon. */
    public void play(PlayJob job) {
        _queue.add(job);
    }

    @Override
    public void run() {
        while (true) {
            // Retrieve next play job.
            PlayJob job = null;
            try {
                job = _queue.take();
            } catch (InterruptedException e) {
                Core.log(Level.WARNING, this + " has died.", e);
                return;
            }
            synchronized (this) {
                _current = job;
            }

            // Execute it.
            try {
                _current.execute();
            } catch (InterruptedException e) {
                _current.getAnimation().animationStopped();
                // The animation was cancelled.
            }

            // Mark as finished.
            synchronized (this) {
                _current = null;
            }
        }
    }

}
