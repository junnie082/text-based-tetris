package ui;

import logic.SettingController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static component.Button.createLogoBtnNext;
import static component.Button.createLogoBtnUp;
import static component.ScreenSize.setWidthHeight;
import static java.lang.System.exit;

public class MainMenuScreen extends JFrame implements ActionListener {


    JButton btnPlay;
    JButton btnItem;
    JButton btnSetting;
    JButton btnRanking;
    JButton btnExit;
    SettingController settingController = new SettingController();

    private String screenSize;

    private int playWidth; // 플레이 버튼 가로
    private int playHeight; // 플레이 버튼 세로

    private int menuWidth; // 메뉴 버튼 가로
    private int menuHeight; // 메뉴 버튼 세로

    private int titleFontSize;

    public MainMenuScreen() {

        try {
            // Set the icon image
            Image iconImage = Toolkit.getDefaultToolkit().getImage("path/to/icon.png"); // Provide the path to your icon file
            setIconImage(iconImage);
        } catch (Exception e) {
            System.err.println("Error loading icon image: " + e.getMessage());
            e.printStackTrace();
        }


        setTitle("Tetris");
        screenSize = settingController.getScreenSize("screenSize", "small");

        switch (screenSize) {
            case "small":
                setWidthHeight(390, 420, this);
                playWidth = 150;
                playHeight = 80;

                menuWidth = 100;
                menuHeight = 100;

                titleFontSize = 70;

                break;
            case "big":
                setWidthHeight(910, 940, this);
                playWidth = 400;
                playHeight = 200;

                menuWidth = 270;
                menuHeight = 200;

                titleFontSize = 150;
                break;
            default:
                setWidthHeight(650, 680, this);
                playWidth = 250;
                playHeight = 130;

                menuWidth = 170;
                menuHeight = 140;

                titleFontSize = 110;
                break;
        }
        setLocationRelativeTo(null); // Centered window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 레이아웃 설정
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // 상단에 Tetris 제목 레이블 추가
        JLabel titleLabel = new JLabel("TETRIS", SwingConstants.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, titleFontSize));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(20));

        // 중앙 패널에 Play 버튼 추가
        JPanel centerPanel = new JPanel();
        add(Box.createVerticalStrut(10));
        btnPlay = createLogoBtnNext("PLAY", "play", this, screenSize, "src/image/play_logo.png");
        btnPlay.setPreferredSize(new Dimension(playWidth, playHeight)); // (150, 80) & (250, 130) & (400, 200)
        btnPlay.setFocusable(true);
        centerPanel.add(btnPlay);

        // btnItem 버튼 추가
        btnItem = createLogoBtnNext("ITEM", "item", this, screenSize, "src/image/mario.png");
        btnItem.setPreferredSize(new Dimension(playWidth, playHeight));
        btnItem.setFocusable(true);
        centerPanel.add(btnItem);

        add(centerPanel, BorderLayout.CENTER);


        // 하단 패널에 Setting, Ranking, Exit 버튼 추가
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout()); // 버튼들이 나란하게 배치되도록 FlowLayout 사용

        btnSetting = createLogoBtnUp("Setting", "setting", this, screenSize, "src/image/setting_logo.png");
        btnSetting.setPreferredSize((new Dimension(menuWidth, menuHeight))); // (100, 100) & (170, 140) & (270, 200)
        btnSetting.setFocusable(true);
        bottomPanel.add(btnSetting);

        btnRanking = createLogoBtnUp("Rank", "ranking", this, screenSize, "src/image/ranking.png");
        btnRanking.setPreferredSize((new Dimension(menuWidth, menuHeight)));
        btnRanking.setFocusable(true);
        bottomPanel.add(btnRanking);

        btnExit = createLogoBtnUp("Exit", "exit", this, screenSize, "src/image/door.png");
        btnExit.setPreferredSize((new Dimension(menuWidth, menuHeight)));
        btnExit.setFocusable(true);
        bottomPanel.add(btnExit);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Set initial focus to the "Game Start" button after the GUI is fully initialized
        btnPlay.requestFocusInWindow();

        // Attach a key listener to each button
        btnPlay.addKeyListener(new MyKeyListener());
        btnItem.addKeyListener(new MyKeyListener());
        btnSetting.addKeyListener(new MyKeyListener());
        btnRanking.addKeyListener(new MyKeyListener());
        btnExit.addKeyListener(new MyKeyListener());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        setVisible(false);
        switch (command) {
            case "play" -> {
                // TODO : 난이도 선택 화면으로 넘어가서 GameController 실행
                new DifficultyScreen(false);
            }
            case "item" -> {
                // TODO : 난이도 선택 화면으로 넘어가서 GameController 실행
                new DifficultyScreen(true);
            }
            case "setting" -> {
                new SettingScreen();
            }
            case "ranking" -> {
                new ScoreboardScreen();
            }
            case "exit" -> System.exit(0);
        }
    }

    // Key listener class
    private class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_DOWN) {
                focusDownButton();
            } else if (keyCode == KeyEvent.VK_UP) {
                focusUpButton();
            } else if (keyCode == KeyEvent.VK_LEFT) {
                focusLeftButton();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                focusRightButton();
            } else if (keyCode == KeyEvent.VK_ENTER) {
                moveScreen();
            }
        }
    }

    // 엔터 키
    private void moveScreen() {
        setVisible(false);
        if (btnPlay.isFocusOwner()) {
            new DifficultyScreen(false);
        } else if (btnItem.isFocusOwner()) {
            new DifficultyScreen(true);
        } else if (btnSetting.isFocusOwner()) {
            new SettingScreen();
        } else if (btnRanking.isFocusOwner()) {
            new ScoreboardScreen();
        } else if (btnExit.isFocusOwner()) {
            exit(0);
        }
    }

    // 위 방향키
    private void focusUpButton() {
        if (btnSetting.isFocusOwner()) {
            btnPlay.requestFocusInWindow();
        } else if (btnRanking.isFocusOwner()) {
            btnPlay.requestFocusInWindow();
        } else if (btnExit.isFocusOwner()) {
            btnPlay.requestFocusInWindow();
        }
    }

    // 아래 방향키
    private void focusDownButton() {
        if (btnPlay.isFocusOwner()) {
            btnRanking.requestFocusInWindow();
        } else if (btnItem.isFocusOwner()) {
            btnRanking.requestFocusInWindow();
        }
    }

    // 왼쪽 방향키
    private void focusLeftButton() {
        if (btnItem.isFocusOwner()) {
            btnPlay.requestFocusInWindow();
        } else if (btnExit.isFocusOwner()) {
            btnRanking.requestFocusInWindow();
        } else if (btnRanking.isFocusOwner()) {
            btnSetting.requestFocusInWindow();
        }
    }

    // 오른쪽 방향키
    private void focusRightButton() {
        if (btnPlay.isFocusOwner()) {
            btnItem.requestFocusInWindow();
        } else if (btnSetting.isFocusOwner()) {
            btnRanking.requestFocusInWindow();
        } else if (btnRanking.isFocusOwner()) {
            btnExit.requestFocusInWindow();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(MainMenuScreen::new);
    }
}
