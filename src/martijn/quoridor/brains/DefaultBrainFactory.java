package martijn.quoridor.brains;

import java.util.ArrayList;
import java.util.List;

/**
 * The DefaultBrainFactory provides the default brains used
 * in the Quoridor application.
 */
public class DefaultBrainFactory implements BrainFactory {

    @Override
    public List<Brain> getBrains() {
        List<Brain> brains = new ArrayList<Brain>();

        brains.add(new DumbBrain());
        brains.add(createSmartBrain(2));
        brains.add(createSmartBrain(3));
        brains.add(createSmartBrain(4));

        return brains;
    }

    private Brain createSmartBrain(int i) {
        NegamaxBrain b = new SmartBrain(i);
        b.setDeterministic(false);
        return b;
    }

}
