package ui;

import component.ScreenSize;
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

import static component.Button.createBtn;
import static component.Button.createLogoBtn;
import static component.ScreenSize.setWidthHeight;

public class ScoreboardScreen extends JFrame implements ActionListener {

    SettingController settingController = new SettingController();
    ScoreController scoreController = new ScoreController();
    JButton menuBtn;
    private final String screenSize;

    // 메인메뉴의 스코어 보드
    public ScoreboardScreen() {

        setTitle("Tetris - Ranking"); // 창의 제목 설정
        screenSize = settingController.getScreenSize("screenSize", "small");
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
        setLocationRelativeTo(null); // 창을 화면 가운데에 위치시킴
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫으면 프로그램 종료

        // 메인 패널의 레이아웃 변경
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2)); // 1행 2열 그리드 레이아웃으로 변경

        // 노말 모드 스코어 보드 패널을 포함할 패널
        JPanel normalWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel normalPanel = createScorePanel(false, screenSize);
        normalWrapperPanel.add(normalPanel);
        mainPanel.add(normalWrapperPanel); // 메인 패널에 추가

        // 아이템 모드 스코어 보드 패널을 포함할 패널
        JPanel itemWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel itemPanel = createScorePanel(true, screenSize);
        itemWrapperPanel.add(itemPanel);
        mainPanel.add(itemWrapperPanel); // 메인 패널에 추가

        // 메뉴 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // menuBtn = createBtn("Menu", "menu", this);
        menuBtn = createLogoBtn("menu", this, "src/image/backLogo.png");
        menuBtn.setPreferredSize((new Dimension(60, 32)));
        menuBtn.addKeyListener(new MyKeyListener());
        btnPanel.add(menuBtn);

        // 전체 메인 패널을 담을 새로운 패널 생성 및 레이아웃 설정
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER); // 메인 패널을 중앙에 배치
        contentPanel.add(btnPanel, BorderLayout.SOUTH); // 버튼 패널을 아래쪽에 배치

        // 설정이 끝난 패널을 JFrame에 추가
        add(contentPanel);

        setVisible(true);
    }

    private JPanel createScorePanel(boolean isItem, String screenSize) {

        int titleSize = switch (screenSize) {
            case "small" -> 26;
            case "big" -> 65;
            default -> 45;
        };

        int fontSize = switch (screenSize) {
            case "small" -> 13;
            case "big" -> 25;
            default -> 18;
        };

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(11, 1)); // 제목 행(1칸) + 10개의 스코어(10칸)

        String mode = isItem ? "ITEM" : "NORMAL";
        JLabel title = new JLabel(mode, SwingConstants.CENTER); // 가운데 정렬
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleSize)); // 제목의 폰트 설정
        scorePanel.add(title);

        // 예시 데이터 추가
        List<Score> topScores = scoreController.getScores(isItem);

        // 상위 10개 스코어 표시
        for (int i = 0; i < topScores.size(); i++) {
            Score score = topScores.get(i);
            JLabel scoreLabel = new JLabel((i + 1) + ". " + score.getPlayerName() + "(" + score.getDifficulty() + ")" + " - " + score.getScore(), SwingConstants.CENTER);
            scoreLabel.setFont(new Font(title.getFont().getName(), Font.PLAIN, fontSize));
            scorePanel.add(scoreLabel);
        }
        return scorePanel;
    }

    // Top 성적을 얻어 이름을 등록한 후에 표시할 스코어 보드
    public ScoreboardScreen(Score currScore, boolean isItem) {

        setTitle("Tetris - ScoreBoard"); // 창의 제목 설정

        screenSize = settingController.getScreenSize("screenSize", "small");
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
        setLocationRelativeTo(null); // 창을 화면 가운데에 위치시킴
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫으면 프로그램 종료

        // 메인 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 스코어 보드 패널
        JPanel scorePanel = createScorePanel(currScore, isItem, screenSize);
        scorePanel.repaint();
        mainPanel.add(scorePanel);

        // 메뉴 버튼
        JPanel btnPanel = new JPanel();
        menuBtn = createBtn("Menu", "menu", this);
        btnPanel.add(menuBtn);
        mainPanel.add(btnPanel);

        // 설정이 끝난 패널을 JFrame에 추가
        menuBtn.addKeyListener(new MyKeyListener());
        add(mainPanel);
        setVisible(true);
    }

    // 새로 등록한 점수를 강조할 스코어 패널
    private JPanel createScorePanel(Score currScore, boolean isItem, String screenSize) {

        int titleSize = switch (screenSize) {
            case "small" -> 26;
            case "big" -> 65;
            default -> 45;
        };

        int fontSize = switch (screenSize) {
            case "small" -> 13;
            case "big" -> 25;
            default -> 18;
        };

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(11, 1)); // 제목 행(1칸) + 10개의 스코어(10칸)

        String mode = isItem ? "ITEM" : "NORMAL";
        JLabel title = new JLabel(mode, SwingConstants.CENTER); // 가운데 정렬
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleSize)); // 제목의 폰트 설정
        scorePanel.add(title);

        // 예시 데이터 추가
        List<Score> topScores = scoreController.getScores(isItem);

        // 상위 10개 스코어 표시
        for (int i = 0; i < topScores.size(); i++) {

            Score score = topScores.get(i);
            JLabel scoreLabel = new JLabel((i + 1) + ". " + score.getPlayerName() + "(" + score.getDifficulty() + ")" + " - " + score.getScore(), SwingConstants.CENTER);

            scoreLabel.setFont(new Font(title.getFont().getName(), Font.PLAIN, fontSize));
            if (score.equals(currScore)) {
                scoreLabel.setForeground(Color.BLUE);
            }
            scorePanel.add(scoreLabel);
        }
        return scorePanel;
    }

    class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER) {
                setVisible(false);
                new MainMenuScreen();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("menu")) {
            setVisible(false);
            new MainMenuScreen();
        }
    }
}
