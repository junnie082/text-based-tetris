package ui;

import logic.ScoreController;
import logic.SettingController;
import ui.KeySettingScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static component.Button.createBtn;
import static component.Button.createLogoBtn;
import static component.Panel.createPanel;
import static component.ScreenSize.setWidthHeight;

public class
SettingScreen extends JFrame implements ActionListener {
    JButton btnSize1, btnSize2, btnSize3;
    JButton btnKeySetting;
    JButton btnInitializeKeySetting;
    JButton btnInitializeNormalScore, btnInitializeItemScore;
    JButton btnColorBlind0, btnColorBlind1, btnColorBlind2, btnColorBlind3;
    JButton btnInitializeSetting;
    JButton btnMenu;
    ScoreController scoreController = new ScoreController();
    SettingController settingController = new SettingController();

    public SettingScreen() {

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

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        // Screen Size
        btnSize1 = createBtn("Small", "small", this);
        btnSize2 = createBtn("Medium", "medium", this);
        btnSize3 = createBtn("Big", "big", this);
        btnSize1.addKeyListener(new MyKeyListener());
        btnSize2.addKeyListener(new MyKeyListener());
        btnSize3.addKeyListener(new MyKeyListener());
        settingsPanel.add(createPanel("Screen Size", new JButton[]{btnSize1, btnSize2, btnSize3}, screenSize));

        // Key Setting
        btnKeySetting = createBtn("Key Setting", "keySetting", this);
        btnKeySetting.addKeyListener(new MyKeyListener());

        btnInitializeKeySetting = createBtn("Initialize", "initializeKey", this);
        btnInitializeKeySetting.addKeyListener(new MyKeyListener());

        settingsPanel.add(createPanel("Key Setting", new JButton[]{btnKeySetting, btnInitializeKeySetting}, screenSize));

        // Initialize Scoreboard
        btnInitializeNormalScore = createBtn("Normal", "scoreNormal", this); // Normal
        btnInitializeNormalScore.addKeyListener(new MyKeyListener());

        btnInitializeItemScore = createBtn("Item", "scoreItem", this); // Item
        btnInitializeItemScore.addKeyListener(new MyKeyListener());

        settingsPanel.add(createPanel("Initialize Score Board", new JButton[]{btnInitializeNormalScore, btnInitializeItemScore}, screenSize));

        // Colorblind Mode
        btnColorBlind0 = createBtn("Default", "default", this);
        btnColorBlind1 = createBtn("Red", "protanopia", this);
        btnColorBlind2 = createBtn("Green", "deuteranopia", this);
        btnColorBlind3 = createBtn("Blue", "tritanopia", this);
        btnColorBlind0.addKeyListener(new MyKeyListener());
        btnColorBlind1.addKeyListener(new MyKeyListener());
        btnColorBlind2.addKeyListener(new MyKeyListener());
        btnColorBlind3.addKeyListener(new MyKeyListener());
        settingsPanel.add(createPanel("Colorblind Mode", new JButton[]{btnColorBlind0, btnColorBlind1, btnColorBlind2, btnColorBlind3}, screenSize));

        // Initialize Setting
        btnInitializeSetting = createBtn("Initialize", "initialize", this);
        btnInitializeSetting.addKeyListener(new MyKeyListener());
        settingsPanel.add(createPanel("Initialize Setting", new JButton[]{btnInitializeSetting}, screenSize));

        // Back to Main Menu
        // btnMenu = createBtn("Menu", "back", this);
        btnMenu = createLogoBtn("menu", this, "src/image/backLogo.png");
        btnMenu.setPreferredSize((new Dimension(60, 32)));
        btnMenu.addKeyListener(new MyKeyListener());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.add(btnMenu);

        add(settingsPanel, BorderLayout.CENTER);
        add(backPanel, BorderLayout.SOUTH);

        setVisible(true);
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
                applySetting();
            }
        }
    }

    private void applySetting() {

        if (btnSize1.isFocusOwner()) {
            settingController.saveSettings("screenSize", "small");
            setVisible(false);
            new SettingScreen();
        } else if (btnSize2.isFocusOwner()) {
            settingController.saveSettings("screenSize", "medium");
            setVisible(false);
            new SettingScreen();
        } else if (btnSize3.isFocusOwner()) {
            settingController.saveSettings("screenSize", "big");
            setVisible(false);
            new SettingScreen();
        } else if (btnKeySetting.isFocusOwner()) {
            setVisible(false);
            // keySetting 창을 위한 컨트롤러를 하나 만들어야 할 듯
            new KeySettingScreen();
        } else if (btnInitializeKeySetting.isFocusOwner()) {
            settingController.initializeKeySettings();
        } else if (btnInitializeNormalScore.isFocusOwner()) {
            scoreController.resetScores(false);
        } else if (btnInitializeItemScore.isFocusOwner()) {
            scoreController.resetScores(true);
        } else if (btnInitializeSetting.isFocusOwner()) {
            settingController.initializeSettings();
            setVisible(false);
            new SettingScreen();
        } else if (btnColorBlind0.isFocusOwner()) {
            // 기본
            settingController.setColorBlindMode("default");
        } else if (btnColorBlind1.isFocusOwner()) {
            // 적색맹
            settingController.setColorBlindMode("protanopia");
        } else if (btnColorBlind2.isFocusOwner()) {
            // 녹색맹
            settingController.setColorBlindMode("deuteranopia");
        } else if (btnColorBlind3.isFocusOwner()) {
            // 청색맹
            settingController.setColorBlindMode("tritanopia");
        } else if (btnMenu.isFocusOwner()) {
            setVisible(false);
            new MainMenuScreen();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("small")) {
            settingController.saveSettings("screenSize", "small");
            setVisible(false);
            new SettingScreen();
        } else if (command.equals("medium")) {
            settingController.saveSettings("screenSize", "medium");
            setVisible(false);
            new SettingScreen();
        } else if (command.equals("big")) {
            settingController.saveSettings("screenSize", "big");
            setVisible(false);
            new SettingScreen();
        } else if (command.equals("keySetting")) {
            setVisible(false);
            // keySetting 창을 위한 컨트롤러를 하나 만들어야 할 듯
            new KeySettingScreen();
        } else if (command.equals("initializeKey")) {
            settingController.initializeKeySettings();
        } else if (command.equals("menu")) {
            setVisible(false);
            new MainMenuScreen();
        } else if (command.equals("scoreNormal")) {
            scoreController.resetScores(false);
        } else if (command.equals("scoreItem")) {
            scoreController.resetScores(true);
        } else if (command.equals("initialize")) {
            settingController.initializeSettings();
            setVisible(false);
            new SettingScreen();
        } else if (command.equals("default")) {
            // 기본
            settingController.setColorBlindMode("default");
        } else if (command.equals("colorBlind1")) {
            // 적색맹
            settingController.setColorBlindMode("protanopia");
        } else if (command.equals("colorBlind2")) {
            // 녹색맹
            settingController.setColorBlindMode("deuteranopia");
        } else if (command.equals("colorBlind3")) {
            // 청색맹
            settingController.setColorBlindMode("tritanopia");
        }
    }

    private void focusDownButton() {
        if (btnSize1.isFocusOwner() || btnSize2.isFocusOwner() || btnSize3.isFocusOwner()) {
            btnKeySetting.requestFocusInWindow();
        } else if (btnKeySetting.isFocusOwner() || btnInitializeKeySetting.isFocusOwner()) {
            btnInitializeNormalScore.requestFocusInWindow();
        } else if (btnInitializeNormalScore.isFocusOwner() || btnInitializeItemScore.isFocusOwner()) {
            btnColorBlind1.requestFocusInWindow();
        } else if (btnColorBlind0.isFocusOwner() || btnColorBlind1.isFocusOwner() || btnColorBlind2.isFocusOwner() || btnColorBlind3.isFocusOwner()) {
            btnInitializeSetting.requestFocusInWindow();
        } else if (btnInitializeSetting.isFocusOwner()) {
            btnMenu.requestFocusInWindow();
        }
    }

    private void focusUpButton() {
        if (btnMenu.isFocusOwner()) {
            btnInitializeSetting.requestFocusInWindow();
        } else if (btnInitializeSetting.isFocusOwner()) {
            btnColorBlind2.requestFocusInWindow();
        } else if (btnColorBlind0.isFocusOwner() ||btnColorBlind1.isFocusOwner() || btnColorBlind2.isFocusOwner() || btnColorBlind3.isFocusOwner()) {
            btnInitializeNormalScore.requestFocusInWindow();
        } else if (btnInitializeNormalScore.isFocusOwner() || btnInitializeItemScore.isFocusOwner()) {
            btnKeySetting.requestFocusInWindow();
        } else if (btnKeySetting.isFocusOwner() || btnInitializeKeySetting.isFocusOwner()) {
            btnSize2.requestFocusInWindow();
        }
    }

    private void focusRightButton() {
        if (btnSize1.isFocusOwner()) {
            btnSize2.requestFocusInWindow();
        } else if (btnSize2.isFocusOwner()) {
            btnSize3.requestFocusInWindow();
        } else if (btnKeySetting.isFocusOwner()) {
            btnInitializeKeySetting.requestFocusInWindow();
        } else if (btnInitializeNormalScore.isFocusOwner()) {
            btnInitializeItemScore.requestFocusInWindow();
        } else if (btnColorBlind0.isFocusOwner()) {
            btnColorBlind1.requestFocusInWindow();
        } else if (btnColorBlind1.isFocusOwner()) {
            btnColorBlind2.requestFocusInWindow();
        } else if (btnColorBlind2.isFocusOwner()) {
            btnColorBlind3.requestFocusInWindow();
        }
    }

    private void focusLeftButton() {
        if (btnSize2.isFocusOwner()) {
            btnSize1.requestFocusInWindow();
        } else if (btnSize3.isFocusOwner()) {
            btnSize2.requestFocusInWindow();
        } else if (btnInitializeKeySetting.isFocusOwner()) {
            btnKeySetting.requestFocusInWindow();
        } else if (btnInitializeItemScore.isFocusOwner()) {
            btnInitializeNormalScore.requestFocusInWindow();
        } else if (btnColorBlind3.isFocusOwner()) {
            btnColorBlind2.requestFocusInWindow();
        } else if (btnColorBlind2.isFocusOwner()) {
            btnColorBlind1.requestFocusInWindow();
        } else if (btnColorBlind1.isFocusOwner()) {
            btnColorBlind0.requestFocusInWindow();
        }
    }

    // 테스트 코드 작성을 위한 메서드
    public SettingController getSettingController() {

        return this.settingController;
    }
}
