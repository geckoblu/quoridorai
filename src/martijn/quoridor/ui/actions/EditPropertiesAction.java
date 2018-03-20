package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import martijn.quoridor.Config;
import martijn.quoridor.I18N;
import martijn.quoridor.ui.PropertiesPanel;

@SuppressWarnings("serial")
public class EditPropertiesAction extends AbstractAction {

    Component _parent;

    public EditPropertiesAction(Component parent) {
        super();

        I18N.Action action = I18N.getAction("EDITPROPERTIES");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
        putValue(Action.SHORT_DESCRIPTION, action.short_description);

        this._parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel message = new PropertiesPanel();

        String title = I18N.tr("PROPERTIES");
        Object[] options = { I18N.tr("CLOSE") };
        JOptionPane.showOptionDialog(_parent,
                                     message,
                                     title,
                                     JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.PLAIN_MESSAGE,
                                     null,
                                     options,
                                     options[0]);

        Config.save();
        _parent.repaint();
    }

}
