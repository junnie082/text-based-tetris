package logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
class SettingControllerTest {
    private SettingController settingController;
    private Properties originalSettings;

    @BeforeEach
    void setUp() {
        settingController = new SettingController();

        // 테스트 전에 현재 설정 값을 저장
        originalSettings = new Properties();
        try (FileInputStream fis = new FileInputStream(SettingProperties.configPath)) {
            originalSettings.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 기본 설정으로 초기화
        settingController.initializeSettings();
        settingController.initializeKeySettings();
    }

    @AfterEach
    void tearDown() {
        // 테스트 후에 원래 설정 값을 복원
        try (FileOutputStream fos = new FileOutputStream(SettingProperties.configPath)) {
            originalSettings.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getScreenSize() {
        String screenSize = settingController.getScreenSize("screenSize", "medium");
        assertEquals("medium", screenSize, "Default screen size should be 'medium'.");
    }

    @Test
    void getColorMode() {
        String colorMode = settingController.getColorMode("colorMode", "one");
        assertEquals("one", colorMode, "Default color mode should be 'one'.");
    }

    @Test
    void getCellSize_small() {
        int cellSize = settingController.getCellSize("small");
        assertEquals(15, cellSize, "Cell size for 'small' should be 15.");
    }

    @Test
    void getCellSize_medium() {
        int cellSize = settingController.getCellSize("medium");
        assertEquals(25, cellSize, "Cell size for 'medium' should be 25.");
    }

    @Test
    void getCellSize_big() {
        int cellSize = settingController.getCellSize("big");
        assertEquals(35, cellSize, "Cell size for 'big' should be 35.");
    }

    @Test
    void getScoreSize_small() {
        int scoreSize = settingController.getScoreSize("small");
        assertEquals(20, scoreSize, "Score size for 'big' should be 20.");
    }

    @Test
    void getScoreSize_medium() {
        int scoreSize = settingController.getScoreSize("medium");
        assertEquals(40, scoreSize, "Score size for 'medium' should be 40.");
    }

    @Test
    void getScoreSize_big() {
        int scoreSize = settingController.getScoreSize("big");
        assertEquals(60, scoreSize, "Score size for 'big' should be 60.");
    }

    @Test
    void getMessageSize_small() {
        int messageSize = settingController.getMessageSize("small");
        assertEquals(12, messageSize, "Message size for 'medium' should be 12.");
    }

    @Test
    void getMessageSize_medium() {
        int messageSize = settingController.getMessageSize("medium");
        assertEquals(17, messageSize, "Message size for 'medium' should be 17.");
    }

    @Test
    void getMessageSize_big() {
        int messageSize = settingController.getMessageSize("big");
        assertEquals(22, messageSize, "Message size for 'medium' should be 22.");
    }

    @Test
    void getBlockSize_small() {
        int blockSize = settingController.getBlockSize("small");
        assertEquals(22, blockSize, "Block size for 'big' should be 22.");
    }

    @Test
    void getBlockSize_medium() {
        int blockSize = settingController.getBlockSize("medium");
        assertEquals(36, blockSize, "Block size for 'big' should be 36.");
    }

    @Test
    void getBlockSize_big() {
        int blockSize = settingController.getBlockSize("big");
        assertEquals(48, blockSize, "Block size for 'big' should be 48.");
    }

    @Test
    void getDifficulty() {
        int difficulty = settingController.getDifficulty();
        assertEquals(1, difficulty, "Default difficulty should be 1.");
    }

    @Test
    void getKeyCodes() {
        int[] keyCodes = settingController.getKeyCodes();
        assertEquals(38, keyCodes[0], "Default rotate key should be KeyEvent.VK_UP.");
        assertEquals(37, keyCodes[1], "Default left key should be KeyEvent.VK_LEFT.");
        assertEquals(39, keyCodes[2], "Default right key should be KeyEvent.VK_RIGHT.");
        assertEquals(40, keyCodes[3], "Default down key should be KeyEvent.VK_DOWN.");
        assertEquals(32, keyCodes[4], "Default drop key should be KeyEvent.VK_SPACE.");
    }

    @Test
    void setKeyCodes() {
        int[] newKeyCodes = {87, 65, 68, 83, 32}; // W, A, D, S, Space
        settingController.setKeyCodes(newKeyCodes);

        int[] keyCodes = settingController.getKeyCodes();
        assertArrayEquals(newKeyCodes, keyCodes, "Key codes should match the set values.");
    }

    @Test
    void getKeyShape() {
        String[] keyShapes = settingController.getKeyShape();
        assertEquals("↑", keyShapes[0], "Default rotate shape should be UP arrow.");
    }

    @Test
    void setKeyShape() {
        String[] newKeyShapes = {"W", "A", "D", "S", " "}; // New key shapes
        settingController.setKeyShape(newKeyShapes);

        String[] keyShapes = settingController.getKeyShape();
        assertArrayEquals(newKeyShapes, keyShapes, "Key shapes should match the set values.");
    }

    @Test
    void saveSettings() {
        settingController.saveSettings("testKey", "testValue");
        String savedValue = settingController.getScreenSize("testKey", "default");
        assertEquals("testValue", savedValue, "The saved value should be 'testValue'.");
    }

    @Test
    void initializeKeySettings() {
        settingController.initializeKeySettings();
        int[] keyCodes = settingController.getKeyCodes();

        assertEquals(38, keyCodes[0], "Rotate key after initialization should be KeyEvent.VK_UP.");
    }

    @Test
    void initializeSettings() {
        settingController.initializeSettings();
        String screenSize = settingController.getScreenSize("screenSize", "default");
        assertEquals("medium", screenSize, "After initialization, screen size should be 'medium'.");
    }

    @Test
    void setColorBlindMode() {
        settingController.setColorBlindMode("modeTwo");
        String colorMode = settingController.getColorMode("colorBlindMode", "default");
        assertEquals("modeTwo", colorMode, "Color blind mode should be set to 'modeTwo'.");
    }
}
