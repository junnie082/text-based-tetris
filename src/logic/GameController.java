package logic;

import model.Direction;
import model.WeightItemBlock;
import ui.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// 게임의 전반적인 흐름을 제어하는 클래스
public class GameController implements PauseScreenCallback {
    private BoardController boardController;
    private InGameScreen inGameScreen;
    private final ScoreController scoreController;
    private final SettingController settingController = new SettingController();
    private final int[] keyCodes = settingController.getKeyCodes();
    private int ROTATE = keyCodes[0];
    private int LEFT = keyCodes[1];
    private int RIGHT = keyCodes[2];
    private int DOWN = keyCodes[3];
    private int DROP = keyCodes[4];
    JFrame frame;
    Timer timer;


    final int MAX_SPEED = 200;

    int currentSpeed;
    private boolean isItem;

    // 게임 컨트롤러 생성자
    public GameController(boolean isItem) {

        // 노말 모드 vs 아이템 모드
        this.isItem = isItem;

        initUI();
        this.boardController = new BoardController(this, isItem);
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
        timer.start();
    }

    @Override
    public void onHideFrame() {
        frame.setVisible(false);
    }

    // 키보드 이벤트 처리
    // TODO: 3/24/24 : 효정이가 KeyListener 구현 하면 바꿀 예정
    private void setupKeyListener(JFrame frame) {
        // Create the PauseScreen instance once during initialization

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Use the custom key codes for control
                if (e.getKeyCode() == LEFT) {
                    boardController.moveBlock(Direction.LEFT);
                } else if (e.getKeyCode() == RIGHT) {
                    boardController.moveBlock(Direction.RIGHT);
                } else if (e.getKeyCode() == DOWN) {
                    boardController.moveBlock(Direction.DOWN);
                    inGameScreen.updateBoard();
                } else if (e.getKeyCode() == ROTATE) {
                    boardController.moveBlock(Direction.UP); // Consider renaming Direction.UP to ROTATE for clarity
                } else if (e.getKeyCode() == DROP) {
                    boardController.moveBlock(Direction.SPACE); // Consider renaming Direction.SPACE to DROP for clarity
                    inGameScreen.updateBoard();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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
        boardController.placeNewBlock();

        timer = new Timer(currentSpeed, e -> {
            boolean blink = boardController.blinkCheck();
            if(blink) {
                while (blink) {
                    inGameScreen.updateBoard();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    blink = boardController.blinkCheck();
                }
            }

            inGameScreen.updateBoard();
            if(boardController.getNewBlockState()) boardController.placeNewBlock();
            boardController.moveBlock(Direction.DOWN);
            inGameScreen.updateBoard();


            if (boardController.checkGameOver()) {
                frame.dispose();
                if (scoreController.isScoreInTop10(boardController.getScore(), isItem)) {
                    new RegisterScoreScreen(boardController.getScore(), isItem);
                } else {
                    new GameOverScreen(boardController.getScore(), isItem);
                }
                timer.stop();
            }
        });
        timer.start();
    }

    public void speedUp(int speed) {
        if (currentSpeed >= MAX_SPEED) {
            if(settingController.getDifficulty()==0) speed = (int)(speed * 0.8);
            if(settingController.getDifficulty()==2) speed = (int)(speed * 1.2);

            currentSpeed -= speed;
            timer.setDelay(currentSpeed);
            boardController.addScoreMessage("Speed up! \nCurrent Delay " + currentSpeed);
        }
    }
}
