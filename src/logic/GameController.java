package logic;

import model.Direction;
import ui.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.CharArrayReader;
import java.util.ArrayList;

// 게임의 전반적인 흐름을 제어하는 클래스
public class GameController implements PauseScreenCallback {
    private BoardController boardController;
    private InGameScreen inGameScreen;
    private final ScoreController scoreController;
    private JFrame frame;
    private Timer timer;

    final int MAX_SPEED = 500;

    private int currentSpeed;
    private boolean isItem;

    // 게임 컨트롤러 생성자
    public GameController(boolean isItem) {

        // 노말 모드 vs 아이템 모드
        this.isItem = isItem;

        initUI();
        this.boardController = new BoardController(this);
        this.inGameScreen = new InGameScreen(this.boardController);
        this.scoreController = new ScoreController();

        startGame(isItem);
    }

    // 게임 UI 초기화
    private void initUI() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Tetris Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(inGameScreen);
            frame.pack();
            frame.setLocationRelativeTo(null);
            setupKeyListener(frame);
            frame.setVisible(true);
            frame.setResizable(false);

            // 콘솔에서 상태 확인을 위한 임시 코드
            // 실제 게임에서는 게임 로직에 따라 점수를 업데이트하게 됩니다.
            // TODO: 3/24/24 : 현재 점수 계산 로직 없음, BoardController 또는 GameController에서 점수 계산 로직 추가 필요
            inGameScreen.updateScore(0); // 점수를 임시로 0으로 설정
        });
    }

    @Override
    public void onResumeGame() {
        System.out.println("On Resume Game");
        timer.start();
    }

    @Override
    public void onHideFrame() {
        System.out.println("On Hide Frame");
        frame.setVisible(false);
    }

    // 키보드 이벤트 처리
    // TODO: 3/24/24 : 효정이가 KeyListener 구현 하면 바꿀 예정
    private void setupKeyListener(JFrame frame) {
        System.out.println("setupkeylistener");

        SettingController settingController = new SettingController();
        ArrayList<String> keyList = settingController.getKeys();

        String changeShapeKey = keyList.get(0);
        String leftKey = keyList.get(1);
        String rightKey = keyList.get(2);
        String goDownFasterKey = keyList.get(3);
        String goDownAtOnceKey = keyList.get(4);
        // ← 37  ↑ 38  → 39  ↓ 40  ␣ 32  `  192  - 45

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int eventCode = e.getExtendedKeyCode();
                String keyString = KeyEvent.getKeyText(eventCode);
//                System.out.println("keyString: " + keyString);
//                System.out.println("changeShapeKey: " + changeShapeKey + " leftKey: " + leftKey
//                        + " rightKey: " + rightKey + " goDownFasterKey: " + goDownFasterKey + " goDownAtOnceKey: " + goDownAtOnceKey);
                if (keyString.equals(changeShapeKey)) {
                    boardController.moveBlock(Direction.UP);
                } else if (keyString.equals(leftKey)) {
                    boardController.moveBlock(Direction.LEFT);
                } else if (keyString.equals(rightKey)) {
                    boardController.moveBlock(Direction.RIGHT);
                } else if (keyString.equals(goDownFasterKey)) {
                    boardController.moveBlock(Direction.DOWN);
                    inGameScreen.updateBoard();
                } else if (keyString.equals(goDownAtOnceKey)) {
                    boardController.moveBlock(Direction.SPACE);
                    inGameScreen.updateBoard();
                } else if (eventCode == KeyEvent.VK_ESCAPE) {
                    timer.stop();
                    PauseScreen pauseScreen = new PauseScreen(isItem);
                    pauseScreen.setCallback(GameController.this); // Set the callback
                    pauseScreen.setVisible(true); // Show the PauseScreen
                }

                inGameScreen.repaint();
            }
        });
    }


    private void startGame(boolean isItem) {
        currentSpeed = 1000;
        timer = new Timer(currentSpeed, e -> {
            boardController.moveBlock(Direction.DOWN);
            inGameScreen.updateBoard(); // Assuming InGameScreen has a method to update the UI based on the current game state
            if(boardController.checkGameOver()){
                frame.dispose();
                if(scoreController.isScoreInTop10(boardController.getScore(), isItem)){
                    new RegisterScoreScreen(boardController.getScore(), isItem);
                } else {
                    new GameOverScreen(boardController.getScore(), isItem);
                }
                timer.stop();
            }
        });
        timer.start();
    }

    public void speedUp(int speed){
        if(currentSpeed >= MAX_SPEED){
            currentSpeed -= speed;
            timer.setDelay(currentSpeed);
            boardController.addScoreMessage("Speed up! \nCurrent Delay " + currentSpeed);
        }
    }
}
