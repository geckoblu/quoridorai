package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import martijn.quoridor.I18N;
import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.PointOfView;
import martijn.quoridor.ui.actions.AboutAction;
import martijn.quoridor.ui.actions.EditPropertiesAction;
import martijn.quoridor.ui.actions.ExitAction;
import martijn.quoridor.ui.actions.LoadAction;
import martijn.quoridor.ui.actions.NewGameAction;
import martijn.quoridor.ui.actions.RulesAction;
import martijn.quoridor.ui.actions.SaveAction;

@SuppressWarnings("serial")
public class ApplicationFrame extends JFrame {

    private GamePanel _gamePanel;

    public ApplicationFrame(BrainFactory factory) {

        initUI(factory);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    private void initUI(BrainFactory factory) {
        setTitle("QuoridorAI");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        URL url = getClass().getResource("/icons/quoridor.png");
        Image icon = Toolkit.getDefaultToolkit().getImage(url);
        setIconImage(icon);

        Board board = new Board();


        StatusBar statusbar = new StatusBar();
        add(statusbar, BorderLayout.SOUTH);

        _gamePanel = new GamePanel(board, factory, statusbar);
        add(_gamePanel, BorderLayout.CENTER);

        createMenuBar(board);
    }

    private void createMenuBar(Board board) {

        JMenuBar menubar = new JMenuBar();

        createFileMenu(menubar, board);
        createViewMenu(menubar);
        createHelpMenu(menubar);

        setJMenuBar(menubar);
    }

    private void createFileMenu(JMenuBar menubar, Board board) {

        I18N.Menu i18nMenu = I18N.getMenu("FILE");
        JMenu fileMenu = new JMenu(i18nMenu.label);
        fileMenu.setMnemonic(i18nMenu.mnemonic);

        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(new NewGameAction(board));
        fileMenu.add(menuItem);

        fileMenu.addSeparator();

        menuItem = new JMenuItem();
        menuItem.setAction(new SaveAction(this, board));
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setAction(new LoadAction(this, board));
        fileMenu.add(menuItem);

        fileMenu.addSeparator();

        menuItem = new JMenuItem();
        menuItem.setAction(new EditPropertiesAction(this));
        fileMenu.add(menuItem);

        fileMenu.addSeparator();

        menuItem = new JMenuItem();
        menuItem.setAction(new ExitAction(this));

        fileMenu.add(menuItem);

        menubar.add(fileMenu);
    }

    private void createViewMenu(JMenuBar menubar) {

        I18N.Menu i18nMenu = I18N.getMenu("VIEW");
        JMenu viewMenu = new JMenu(i18nMenu.label);
        viewMenu.setMnemonic(i18nMenu.mnemonic);

        JMenu povMenu = new JMenu(I18N.tr("POINT_OF_VIEW"));
        //menuItem.setAction(new AboutAction(this));
        viewMenu.add(povMenu);

        ButtonGroup group = new ButtonGroup();

        final JMenuItem menuItem1 = new JRadioButtonMenuItem(new AbstractAction(I18N.tr("PLAYER_1")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPointOfView(PointOfView.POV1);
            }
        });
        povMenu.add(menuItem1);
        group.add(menuItem1);
        menuItem1.setSelected(true);

        final JMenuItem menuItem2 = new JRadioButtonMenuItem(new AbstractAction(I18N.tr("PLAYER_2")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPointOfView(PointOfView.POV2);
            }
        });
        povMenu.add(menuItem2);
        group.add(menuItem2);

        _gamePanel.addPropertyChangeListener("POINT_OF_VIEW", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                menuItem1.setSelected(evt.getNewValue().equals(PointOfView.POV1));
                menuItem2.setSelected(evt.getNewValue().equals(PointOfView.POV2));
            }
        });


        menubar.add(viewMenu);
    }

    private void createHelpMenu(JMenuBar menubar) {

        I18N.Menu i18nMenu = I18N.getMenu("HELP");
        JMenu helpMenu = new JMenu(i18nMenu.label);
        helpMenu.setMnemonic(i18nMenu.mnemonic);

        JMenuItem menuItem = new JMenuItem();
        menuItem.setAction(new RulesAction(this));
        helpMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setAction(new AboutAction(this));
        helpMenu.add(menuItem);

        menubar.add(helpMenu);
    }

    public final void close() {
        dispose();
        System.exit(0);
    }

    private void setPointOfView(PointOfView pointOfView) {
        _gamePanel.setPointOfView(pointOfView);
    }

}
