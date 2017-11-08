package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import martijn.quoridor.I18N;
import martijn.quoridor.ui.AboutCard;

@SuppressWarnings("serial")
public class AboutAction extends AbstractAction {
	
	private final Component parent;
	
	public AboutAction(Component parent) {
		super();
		
		I18N.Action action = I18N.getAction("ABOUT");
		putValue(Action.NAME, action.name);
		putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
		putValue(Action.SHORT_DESCRIPTION, action.short_description);
		
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(parent,
				                      new AboutCard(),
				                      I18N.tr("ABOUT_QUORIDORAI"),
				                      JOptionPane.PLAIN_MESSAGE);
	}

}
