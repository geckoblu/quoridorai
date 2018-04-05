package martijn.quoridor.brains;

import java.util.List;

/**
 * A {@code BrainFactory} creates the brains used in the Quoridor application.
 */
public interface BrainFactory {

    /**
     * Returns the list of brains.
     */
    List<Brain> getBrains();

}
