package ui;

import logic.GameController;
import logic.SettingController;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static component.Button.*;
import static component.ScreenSize.setWidthHeight;

public class DifficultyScreen extends JFrame implements ActionListener {

    JButton btnEasy, btnNormal, btnHard;
    JButton btnGeneral, btnItem;
    JButton btnMenu;
    boolean isItem;
    SettingController settingController = new SettingController();

    private int btnSize;

    public DifficultyScreen(boolean isItem) {

        // 노말 모드 vs 아이템 모드
        this.isItem = isItem;

        setTitle("Tetris - Difficulty");
        String screenSize = settingController.getScreenSize("screenSize", "small");
        switch (screenSize) {
            case "small":
                setWidthHeight(390, 420, this);
                btnSize = 100;
                break;
            case "big":
                setWidthHeight(910, 940, this);
                btnSize = 200;
                break;
            default:
                setWidthHeight(650, 680, this);
                btnSize = 170;
                break;
        }
        setLocationRelativeTo(null); // Centered window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 레이아웃 설정
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // 버튼
        JPanel topPanel = new JPanel();
        btnEasy = createLogoBtnUp("Easy", "easy", this, screenSize, "src/image/easy_logo.png");
        btnEasy.setPreferredSize((new Dimension(btnSize, btnSize)));
        btnNormal = createLogoBtnUp("Normal", "normal", this, screenSize, "src/image/normal_logo.png");
        btnNormal.setPreferredSize((new Dimension(btnSize, btnSize)));
        btnHard = createLogoBtnUp("Hard", "hard", this, screenSize, "src/image/hard_logo.png");
        btnHard.setPreferredSize((new Dimension(btnSize, btnSize)));

        btnEasy.addKeyListener(new MyKeyListener());
        btnNormal.addKeyListener(new MyKeyListener());
        btnHard.addKeyListener(new MyKeyListener());

        topPanel.add(btnEasy);
        topPanel.add(btnNormal);
        topPanel.add(btnHard);

        add(topPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        // btnMenu = createBtn("Menu", "menu", this);
        btnMenu = createLogoBtn("menu", this, "src/image/backLogo.png");
        btnMenu.setPreferredSize((new Dimension(60, 32)));
        btnMenu.addKeyListener(new MyKeyListener());

        bottomPanel.add(btnMenu);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // 위치를 화면 중앙으로 설정
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        setVisible(false);
        switch (command) {
            case "easy" -> {
                settingController.saveSettings("difficulty", "0");
                new GameController(isItem);
            }
            case "normal" -> {
                settingController.saveSettings("difficulty", "1");
                new GameController(isItem);
            }
            case "hard" -> {
                settingController.saveSettings("difficulty", "2");
                new GameController(isItem);
            }
            case "menu" -> {
                new MainMenuScreen();
            }
        }
    }

    private void moveScreen() {
        setVisible(false);
        if (btnEasy.isFocusOwner()) {
            settingController.saveSettings("difficulty", "0");
            setVisible(false);
            new GameController(isItem);
        } else if (btnNormal.isFocusOwner()) {
            settingController.saveSettings("difficulty", "1");
            setVisible(false);
            new GameController(isItem);
        } else if (btnHard.isFocusOwner()) {
            settingController.saveSettings("difficulty", "2");
            setVisible(false);
            new GameController(isItem);
        } else if (btnMenu.isFocusOwner()) {
            setVisible(false);
            new MainMenuScreen();
        }
    }

    class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_RIGHT) {
                focusRightButton();
            } else if (keyCode == KeyEvent.VK_LEFT) {
                focusLeftButton();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                focusDownButton();
            } else if (keyCode == KeyEvent.VK_UP) {
                focusUpButton();
            } else if (keyCode == KeyEvent.VK_ENTER) {
                moveScreen();
            }
        }
    }

    private void focusRightButton() {
        if (btnEasy.isFocusOwner()) {
            btnNormal.requestFocusInWindow();
        } else if (btnNormal.isFocusOwner()) {
            btnHard.requestFocusInWindow();
        }
    }

    private void focusLeftButton() {
        if (btnHard.isFocusOwner()) {
            btnNormal.requestFocusInWindow();
        } else if (btnNormal.isFocusOwner()) {
            btnEasy.requestFocusInWindow();
        }
    }

    private void focusUpButton() {
        if (btnMenu.isFocusOwner()) {
            btnNormal.requestFocusInWindow();
        }
    }

    private void focusDownButton() {
        if (btnEasy.isFocusOwner() || btnNormal.isFocusOwner() || btnHard.isFocusOwner()) {
            btnMenu.requestFocusInWindow();
        }
    }
}
