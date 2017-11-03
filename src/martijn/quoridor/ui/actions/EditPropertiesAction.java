package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import martijn.quoridor.Config;
import martijn.quoridor.I18N;
import martijn.quoridor.ui.PropertiesPanel;


@SuppressWarnings("serial")
public class EditPropertiesAction extends AbstractAction {

	Component parent; 
	
	public EditPropertiesAction(Component parent) {
		super("Properties");
		this.parent = parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel message = new PropertiesPanel();
		
		String title = I18N.tr("Properties");
		Object[] options = {I18N.tr("Close")};
		JOptionPane.showOptionDialog(parent, message, title,
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
				
		Config.save();		
	}

}
