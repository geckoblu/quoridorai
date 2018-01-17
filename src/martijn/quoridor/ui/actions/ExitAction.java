package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.ui.ApplicationFrame;

@SuppressWarnings("serial")
public class ExitAction extends AbstractAction {

    private final ApplicationFrame application;

    public ExitAction(ApplicationFrame application) {
        super();

        I18N.Action action = I18N.getAction("EXIT");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
        putValue(Action.SHORT_DESCRIPTION, action.short_description);

        this.application = application;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        application.close();
    }

}
