package ui;

import logic.GameController;
import logic.SettingController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static component.Button.createBtn;
import static component.Panel.createPanel;
import static component.ScreenSize.setWidthHeight;
import static java.lang.System.exit;

public class PauseScreen extends JFrame implements ActionListener{
    SettingController settingController = new SettingController();

    JButton btnBack = createBtn("Back", "back", this);
    JButton btnReplay = createBtn("Replay", "replay", this);
    JButton btnMainMenu = createBtn("Menu", "mainMenu", this);
    JButton btnQuit = createBtn("Quit", "quit", this);
    private boolean isItem;


    public PauseScreen(boolean isItem) {

        // 노말모드 vs 아이템 모드
        this.isItem = isItem;

        setTitle("Tetris");
        String screenSize = settingController.getScreenSize("screenSize", "small");
        switch (screenSize) {
            case "small":
                setWidthHeight(390, 420, this);
                break;
            case "big":
                setWidthHeight(910, 940, this);
                break;
            default:
                setWidthHeight(650, 680, this);
                break;
        }
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel pausePannel = new JPanel();
        pausePannel.setLayout(new BoxLayout(pausePannel, BoxLayout.Y_AXIS));

        btnBack.requestFocusInWindow();

        btnBack.addKeyListener(new MyKeyListener());
        btnReplay.addKeyListener(new MyKeyListener());
        btnMainMenu.addKeyListener(new MyKeyListener());
        btnQuit.addKeyListener(new MyKeyListener());

        pausePannel.add(createPanel("Pause", new JButton[]{btnBack, btnReplay, btnMainMenu, btnQuit}));
        add(pausePannel);
        setVisible(true);
    }

    private class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_RIGHT) {
                focusRightButton();
            } else if (keyCode == KeyEvent.VK_LEFT) {
                focusLeftButton();
            }  else if (keyCode == KeyEvent.VK_ENTER) {
                applyPause();
            }
        }
    }

    private void focusRightButton() {
        if (btnBack.isFocusOwner()) {
            btnReplay.requestFocusInWindow();
        } else if (btnReplay.isFocusOwner()) {
            btnMainMenu.requestFocusInWindow();
        } else if (btnMainMenu.isFocusOwner()) {
            btnQuit.requestFocusInWindow();
        }
        else if (btnQuit.isFocusOwner()) {
            btnBack.requestFocusInWindow();
        }
    }

    private void focusLeftButton() {
        if (btnBack.isFocusOwner()) {
            btnQuit.requestFocusInWindow();
        } else if (btnQuit.isFocusOwner()) {
            btnMainMenu.requestFocusInWindow();
        } else if (btnMainMenu.isFocusOwner()) {
            btnReplay.requestFocusInWindow();
        } else if (btnReplay.isFocusOwner()) {
            btnBack.requestFocusInWindow();
        }
    }

    private PauseScreenCallback callback;

    public void setCallback(PauseScreenCallback callback) {
        this.callback = callback;
    }

    private void applyPause() {
        setVisible(false);
        if (btnBack.isFocusOwner()) {
            setVisible(false);
            callback.onResumeGame();
        } else if (btnReplay.isFocusOwner()) {
            new GameController(isItem);
            callback.onHideFrame();
        } else if (btnMainMenu.isFocusOwner()) {
            new MainMenuScreen();
            callback.onHideFrame();
        } else if (btnQuit.isFocusOwner()) {
            exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        setVisible(false);
        if (command.equals("back")) {
            callback.onResumeGame();
        } else if (command.equals("replay")) {
            new GameController(isItem);
            callback.onHideFrame();
        } else if (command.equals("mainMenu")) {
            new MainMenuScreen();
            callback.onHideFrame();
        } else if (command.equals("quit")) {
            exit(0);
        }

    }
}
