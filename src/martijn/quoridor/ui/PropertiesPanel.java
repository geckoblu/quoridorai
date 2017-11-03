package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import martijn.quoridor.Config;
import martijn.quoridor.I18N;

@SuppressWarnings("serial")
public class PropertiesPanel extends JPanel {

	private JRadioButton lamekNotation;
	private JRadioButton glendenningNotation;
	JCheckBox showCoordinates;

	public PropertiesPanel() {
		initUI();

		if (Config.lamekNotation()) {
			lamekNotation.setSelected(true);
		} else {
			glendenningNotation.setSelected(true);
		}
		ActionListener notationActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {								
				Config.lamekNotation(lamekNotation.isSelected());
			}
		};
		lamekNotation.addActionListener(notationActionListener);
		glendenningNotation.addActionListener(notationActionListener);

		showCoordinates.setSelected(Config.showCoordinates());
		showCoordinates.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {				
				Config.showCoordinates(showCoordinates.isSelected());
			}
		});
	}

	private void initUI() {
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel(" "), c); // dummy

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		add(new JLabel("<html><b>" + I18N.tr("Notation") + ":</b></html>"), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		add(new JLabel("  "), c); // dummy

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		ButtonGroup bg = new ButtonGroup();
		lamekNotation = new JRadioButton("Lamek");
		bg.add(lamekNotation);
		add(lamekNotation, c);
		glendenningNotation = new JRadioButton("Glendenning");
		c.gridy = 2;
		bg.add(glendenningNotation);
		add(glendenningNotation, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		add(new JLabel(" "), c); // dummy

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		add(new JLabel("<html><b>" + I18N.tr("Coordinates") + ":</b></html>"), c);

		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		showCoordinates = new JCheckBox(I18N.tr("Show coordinates"));
		add(showCoordinates, c);
	}

//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				UIManager.put("swing.boldMetal", false);
//				
//				try {
//					UIManager.setLookAndFeel(UIManager
//							.getSystemLookAndFeelClassName());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				JPanel p = new JPanel();
//				JPanel contentPane = new JPanel();
//				contentPane.add(p);
//				JFrame parent = new JFrame();
//				parent.setContentPane(contentPane);
//				parent.setSize(400, 300);
//				parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				parent.setVisible(true);
//
//				JPanel message = new PropertiesPanel();
//
//				// JOptionPane.showConfirmDialog(f, message,
//				// "Default made dialog", JOptionPane.YES_NO_OPTION);
//				Object[] options = {"Close"};
//				JOptionPane.showOptionDialog(parent, message, "Properties",
//						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
//						null, options, options[0]);
//
//				System.exit(0);
//
//			}
//		});
//	}
}