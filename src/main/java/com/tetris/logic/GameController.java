package com.tetris.logic;

import com.tetris.model.Direction;
import com.tetris.ui.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// 게임의 전반적인 흐름을 제어하는 클래스
public class GameController implements PauseScreenCallback {

    private String strPlayer;
    private BoardController boardController;
    private InGameScreen inGameScreen;
    private InGameScoreController inGameScoreController;
    private RankScoreController rankScoreController;
    private final SettingController settingController = new SettingController();
    private GameController opponent;
    private final int[] keyCodes = settingController.getKeyCodes();
    private int ROTATE = keyCodes[0];
    private int LEFT = keyCodes[1];
    private int RIGHT = keyCodes[2];
    private int DOWN = keyCodes[3];
    private int DROP = keyCodes[4];
    JFrame frame;
    Timer gameTimer;

    private final int MAX_SPEED = 200;
    private final int NORMAL_TICK = 1000;
    private final int FAST_DELAY = 10;

    public int currentSpeed;
    private boolean isItem;
    private boolean isTimeAttack;
    private boolean isDualMode;

    public void setStrPlayer(String _strPlayer) {
        this.strPlayer = _strPlayer;
    }
    // 게임 컨트롤러 생성자
    public GameController(boolean isItem) {
        this(isItem, false, false);
    }

    public GameController(boolean isItem, boolean isDualMode, boolean isTimeAttack) {
        this.isItem = isItem;
        this.isTimeAttack = isTimeAttack;
        this.isDualMode = isDualMode;
        initUI();
        this.inGameScoreController = new InGameScoreController();
        this.boardController = new BoardController(this, this.inGameScoreController, isItem, isDualMode);
        this.inGameScreen = new InGameScreen(this.boardController, this.inGameScoreController);
        this.rankScoreController = new RankScoreController();
        startGame(isDualMode);
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
            inGameScreen.updateScore(0);
        });
    }

    @Override
    public void onResumeGame() {
        gameTimer.start();
    }

    @Override
    public void onHideFrame() {
        frame.setVisible(false);
    }

    // 키보드 이벤트 처리
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
                } else if (e.getKeyCode() == ROTATE) {
                    boardController.moveBlock(Direction.UP); // Consider renaming Direction.UP to ROTATE for clarity
                } else if (e.getKeyCode() == DROP) {
                    boardController.moveBlock(Direction.SPACE); // Consider renaming Direction.SPACE to DROP for clarity
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameTimer.stop();
                    PauseScreen pauseScreen = new PauseScreen(isItem);
                    pauseScreen.setCallback(GameController.this); // Set the callback
                    pauseScreen.setVisible(true); // Show the PauseScreen
                }
                inGameScreen.updateBoard();
            }
        });
    }

    public void controlGame(String key) {
        switch (key) {
            case "LEFT" -> boardController.moveBlock(Direction.LEFT);
            case "RIGHT" -> boardController.moveBlock(Direction.RIGHT);
            case "DOWN" -> {
                boardController.moveBlock(Direction.DOWN);
            }
            case "ROTATE" -> boardController.moveBlock(Direction.UP);
            case "DROP" -> {
                boardController.moveBlock(Direction.SPACE);
            }
            case "PAUSE" -> gameTimer.stop(); // Todo : 대전 모드 PAUSE 처리
            case "RESUME" -> onResumeGame();
            case "REPLAY" -> {
                frame.dispose();
                new GameController(isItem);
            }
        }
        inGameScreen.updateBoard();
    }

    private void startGame(boolean isDualMode) {
        currentSpeed = NORMAL_TICK;
        boardController.placeNewBlock();
        inGameScreen.updateBoard();
        // 타이머를 게임 루프와 같이 사용합니다.
        setupTimer(currentSpeed);
    }

    private void setupTimer(int delay) {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(delay, e -> gameLoop(isDualMode, true));
        gameTimer.start();
    }

    public void resetTimer() {
        // 현재 설정된 타이머를 중지하고 빠르게 한 번 실행
        if (gameTimer != null) {
            gameTimer.stop();
        }
        // 빠르게 실행할 타이머 설정
        Timer fastTimer = new Timer(FAST_DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // gameLoop를 한 번 실행
                gameLoop(isDualMode, false);
                // 빠르게 실행한 후에 원래 속도의 타이머로 다시 설정
                setupTimer(currentSpeed);
            }
        });
        fastTimer.setRepeats(false);  // 이 타이머는 한 번만 실행됩니다.
        fastTimer.start();
    }

    private void gameLoop(boolean isDualMode, boolean isNotFastTimer) {
        if (isNotFastTimer) {
            boardController.moveBlock(Direction.DOWN);

            while (boardController.blinkCheck()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
                boardController.blinkErase();
                inGameScreen.updateBoard();
            }
        } else {
            boardController.placeBlock();
        }
        inGameScreen.updateBoard();

        // 게임 오버 체크
        if (boardController.checkGameOver()) {
            endGame(isDualMode);
        }
    }

    private void endGame(boolean isDualMode) {
        frame.dispose();
        gameTimer.stop();
        if(!isDualMode){
            if (rankScoreController.isScoreInTop10(inGameScoreController.getScore(), isItem)) {
                new RegisterScoreScreen(inGameScoreController.getScore(), isItem);
            } else {
                new GameOverScreen(inGameScoreController.getScore(), isItem);
            }
        } else {
            // Todo : 대전 모드 일 경우 GameOver 처리
            opponent.sendGameOver();
            new WinnerScreen(opponent.getStrPlayer(), opponent.getScore(), getScore(), isItem, isTimeAttack);
        }
    }

    public void speedUp(int speed) {
        if (currentSpeed >= MAX_SPEED) {
            if (settingController.getDifficulty() == 0) {
                speed = (int) (speed * 0.8);
            } else if (settingController.getDifficulty() == 2) {
                speed = (int) (speed * 1.2);
            }
            currentSpeed -= speed;
            gameTimer.setDelay(currentSpeed);
            inGameScoreController.setScoreOnBlockMoveDown((1100 - currentSpeed) / 100);
            inGameScoreController.addScoreMessage("Speed up! Current speed: " + currentSpeed);
            inGameScoreController.addScoreMessage("Score per Block Move Down: " + inGameScoreController.getScoreOnBlockMoveDown());
        }
    }

    public InGameScreen getInGameScreen() {
        return inGameScreen;
    }

    public String getStrPlayer() {
        return strPlayer;
    }

    public void setOpponent(GameController opponent) {
        this.opponent = opponent;
    }

    public void addLines(int[][] lines) {
        boardController.addLines(lines);
    }

    public void sendLines(int[][] lines) {
        opponent.addLines(lines);
    }

    public int getScore() {
        return inGameScoreController.getScore();
    }

    public void sendGameOver() {
        frame.dispose();
        gameTimer.stop();
    }

    public void gameOver() {
        endGame(true);
    }

}
