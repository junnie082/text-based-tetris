package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController gameController;
    private SettingController settingController;

    @BeforeEach
    void setUp() {
        gameController = new GameController(false); // 초기화
        settingController = new SettingController(); // 초기화
        gameController.frame = new JFrame("Test Frame"); // 프레임 초기화
    }

    @Test
    void onResumeGame() {
        // Arrange
        gameController.timer = new javax.swing.Timer(1000, e -> System.out.println("Timer executed"));

        // Act
        gameController.onResumeGame(); // 게임 재개

        // Assert
        assertTrue(gameController.timer.isRunning(), "Timer should start when game is resumed"); // 타이머가 실행 중인지 확인
    }

    @Test
    void onHideFrame() {
        // Act
        gameController.onHideFrame(); // 프레임 숨기기

        // Assert
        assertFalse(gameController.frame.isVisible(), "Frame should be hidden after onHideFrame is called"); // 프레임이 숨겨졌는지 확인
    }

    @Test
    void speedUp() {
        // Arrange
        gameController.currentSpeed = 1000; // 초기 속도
        gameController.timer = new javax.swing.Timer(1000, e -> System.out.println("Timer executed"));

        // Act
        gameController.speedUp(100); // 속도 올리기

        switch (settingController.getDifficulty()){
            case 0:
                // Assert
                assertEquals(920, gameController.currentSpeed, "Current speed should decrease by the speed increment"); // 속도가 80만큼 감소했는지 확인
                assertEquals(920, gameController.timer.getDelay(), "Timer delay should match the new speed"); // 타이머 지연 시간이 업데이트되었는지 확인
            case 1:
                // Assert
                assertEquals(900, gameController.currentSpeed, "Current speed should decrease by the speed increment"); // 속도가 100만큼 감소했는지 확인
                assertEquals(900, gameController.timer.getDelay(), "Timer delay should match the new speed"); // 타이머 지연 시간이 업데이트되었는지 확인
            case 2:
                // Assert
                assertEquals(880, gameController.currentSpeed, "Current speed should decrease by the speed increment"); // 속도가 120만큼 감소했는지 확인
                assertEquals(880, gameController.timer.getDelay(), "Timer delay should match the new speed"); // 타이머 지연 시간이 업데이트되었는지 확인

        }
    }
}
