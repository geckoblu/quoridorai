package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import martijn.quoridor.I18N;
import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.model.Board;
import martijn.quoridor.ui.actions.AboutAction;
import martijn.quoridor.ui.actions.EditPropertiesAction;
import martijn.quoridor.ui.actions.ExitAction;
import martijn.quoridor.ui.actions.NewGameAction;

@SuppressWarnings("serial")
public class ApplicationFrame extends JFrame {

	private final Board board;
	private final BrainFactory factory;

	public ApplicationFrame(Board board, BrainFactory factory) {
		this.board = board;
		this.factory = factory;

		initUI();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}});
	}

	private void initUI() {
		setTitle("QuoridorAI");
		setSize(500, 600);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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

		I18N.Menu i18nMenu = I18N.getMenu("FILE");
		JMenu file = new JMenu(i18nMenu.label);
        file.setMnemonic(i18nMenu.mnemonic);

        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(new NewGameAction(board));
        file.add(menuItem);

        file.addSeparator();

        menuItem = new JMenuItem();
        menuItem.setAction(new EditPropertiesAction(this));
        file.add(menuItem);

        file.addSeparator();

        menuItem = new JMenuItem();
        menuItem.setAction(new ExitAction(this));

        file.add(menuItem);

        menubar.add(file);
	}

	private void createHelpMenu(JMenuBar menubar) {

		I18N.Menu i18nMenu = I18N.getMenu("HELP");
		JMenu help = new JMenu(i18nMenu.label);
		help.setMnemonic(i18nMenu.mnemonic);

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

	public final void close() {
		dispose();
		System.exit(0);
	}

}
