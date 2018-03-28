package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import martijn.quoridor.I18N;
import martijn.quoridor.ui.RulesPanel;

@SuppressWarnings("serial")
public class RulesAction extends AbstractAction {

    private final Component _parent;

    public RulesAction(Component parent) {
        super();

        I18N.Action action = I18N.getAction("RULES");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        this._parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(_parent,
                                      new RulesPanel(),
                                      I18N.tr("RULES"),
                                      JOptionPane.PLAIN_MESSAGE);
    }

}
