package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import martijn.quoridor.ui.AboutCard;

@SuppressWarnings("serial")
public class AboutAction extends AbstractAction {
	
	private final Component parent;
	
	public AboutAction(Component parent) {
		super("About");
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(parent, new AboutCard(), "About QuoridorAI", JOptionPane.PLAIN_MESSAGE);

	}

}
