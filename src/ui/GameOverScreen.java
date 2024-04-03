package ui;

import logic.Score;
import logic.ScoreController;
import logic.SettingController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import static component.Button.createLogoBtnUp;
import static component.ScreenSize.setWidthHeight;
import static java.lang.System.exit;

public class GameOverScreen extends JFrame implements ActionListener {

    SettingController settingController = new SettingController();
    ScoreController scoreController = new ScoreController();

    JButton btnReplay;
    JButton btnMenu;
    JButton btnExit;

    public GameOverScreen(int score, boolean isItem) {
        setTitle("Tetris - GameOver");
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 창을 화면 가운데에 위치시킵니다.

        // 레이아웃 설정
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // 'Game Over' 문구 레이블
        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font(gameOverLabel.getFont().getName(), Font.BOLD, 50));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(gameOverLabel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(10));

        // 스코어 보드 표시
        JPanel scorePanel = createScorePanel(isItem);
        add(scorePanel, BorderLayout.CENTER);

        // 하단 패널에 Setting, Ranking, Exit 버튼 추가
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout()); // 버튼들이 나란하게 배치되도록 FlowLayout 사용

        btnMenu = createLogoBtnUp("Menu", "menu", this, "src/image/menu.png");
        btnMenu.setPreferredSize((new Dimension(100, 100)));
        btnMenu.setFocusable(true);
        bottomPanel.add(btnMenu);

        btnReplay = createLogoBtnUp("Replay", "replay", this, "src/image/replay.png");
        btnReplay.setPreferredSize((new Dimension(100, 100)));
        btnReplay.setFocusable(true);
        bottomPanel.add(btnReplay);

        btnExit = createLogoBtnUp("Exit", "exit", this, "src/image/exit_logo.png");
        btnExit.setPreferredSize((new Dimension(100, 100)));
        btnExit.setFocusable(true);
        bottomPanel.add(btnExit);

        add(bottomPanel, BorderLayout.SOUTH);

        // UI를 보이게 설정
        setVisible(true);

        // Set initial focus to the "Game Start" button after the GUI is fully initialized
        btnReplay.requestFocusInWindow();

        btnMenu.addKeyListener(new MyKeyListener());
        btnReplay.addKeyListener(new MyKeyListener());
        btnExit.addKeyListener(new MyKeyListener());
    }

    private JPanel createScorePanel(boolean isItem) {

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(11, 1)); // 제목 행(1칸) + 10개의 스코어(10칸)

        JLabel title = new JLabel("Ranking", SwingConstants.CENTER); // 가운데 정렬
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 24)); // 제목의 폰트 설정
        scorePanel.add(title);

        // 예시 데이터 추가
        List<Score> topScores = scoreController.getScores(isItem);

        // 상위 10개 스코어 표시
        for (int i = 0; i < topScores.size(); i++) {
            Score score = topScores.get(i);
            JLabel scoreLabel = new JLabel((i + 1) + ". " + score.getPlayerName() + " - " + score.getScore(), SwingConstants.CENTER);
            scorePanel.add(scoreLabel);
        }
        return scorePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        setVisible(false);
        if (command.equals("menu")) {
            new MainMenuScreen();
        } else if (command.equals("replay")) {
            // TODO : 게임 재개 로직 구현
        } else if (command.equals("exit")) {
            exit(0);
        }
    }

    // Key listener class
    private class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT) {
                focusLeftButton();
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                focusRightButton();
            } else if (keyCode == KeyEvent.VK_ENTER) {
                moveScreen();
            }

            System.out.println("KeyPressed");
        }
    }

    // 엔터 키
    private void moveScreen() {
        setVisible(false);
        if (btnMenu.isFocusOwner()) {
            new MainMenuScreen();
        } else if (btnReplay.isFocusOwner()) {
            // TODO : 게임 재개 로직 구현
        } else if (btnExit.isFocusOwner()) {
            exit(0);
        }
    }

    // 왼쪽 방향키
    private void focusLeftButton() {
        if (btnExit.isFocusOwner()) {
            btnReplay.requestFocusInWindow();
        } else if (btnReplay.isFocusOwner()) {
            btnMenu.requestFocusInWindow();
        }
    }

    // 오른쪽 방향키
    private void focusRightButton() {
        if (btnMenu.isFocusOwner()) {
            btnReplay.requestFocusInWindow();
        } else if (btnReplay.isFocusOwner()) {
            btnExit.requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        // 테스트 목적으로 점수를 100으로 설정하고 게임 종료 화면을 실행합니다.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameOverScreen(100, false);
            }
        });
    }
}
