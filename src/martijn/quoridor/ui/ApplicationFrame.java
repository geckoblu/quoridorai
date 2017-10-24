package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.model.Board;
import martijn.quoridor.ui.actions.AboutAction;
import martijn.quoridor.ui.actions.NewGameAction;

@SuppressWarnings("serial")
public class ApplicationFrame extends JFrame {

	private final Board board;
	private final BrainFactory factory;
	
	public ApplicationFrame(Board board, BrainFactory factory) {
		this.board = board;
		this.factory = factory;
		
		initUI();
	}
	
	private void initUI() {
		setTitle("Quoridor");
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		URL url = getClass().getResource("/icons/quoridor.png");
		Image icon = Toolkit.getDefaultToolkit().getImage(url);
		setIconImage(icon);

		createMenuBar();
		createStatusBar();
		
		GamePanel gamePanel = new GamePanel(board, factory);
		add(gamePanel, BorderLayout.CENTER);
		
	}
	
	private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();

        createFileMenu(menubar);
        createHelpMenu(menubar);
        
        setJMenuBar(menubar);
    }
	
	private void createFileMenu(JMenuBar menubar) {
		
		JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(new NewGameAction(board));
        file.add(menuItem);

        file.addSeparator();
        
        ImageIcon icon = new ImageIcon("exit.png");
        Action exitAction = new AbstractAction("Exit", icon) {
        	public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit application");
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

        menuItem = new JMenuItem();
        menuItem.setAction(exitAction);

        file.add(menuItem);
		
        menubar.add(file);
	}
	
	private void createHelpMenu(JMenuBar menubar) {
		
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(new AboutAction(this));
        help.add(menuItem);
		
		menubar.add(help);
	}
	
	private void createStatusBar() {
		
		JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    statusBar.setBorder(BorderFactory.createEtchedBorder());
	    JLabel status = new JLabel(" ");
	    statusBar.add(status);
	    
	    add(statusBar, BorderLayout.SOUTH);
	    
	}

}
