package logic;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import static logic.SettingProperties.*;

public class SettingController {
    private final Properties properties;

    // Screen을 위한 사이즈 변수
    private String screenSize;

    public SettingController() {
        properties = new Properties();
        loadSettings();
    }

    // 스크린 크기 받아오기
    public String getScreenSize(String key, String defaultValue) {

        // screenSize=small/medium/big
        return properties.getProperty(key, defaultValue);
    }

    // 색맹모드 받아오기
    public String getColorMode(String key, String defaultValue) {

        // colorMode=one/two/three
        return properties.getProperty(key, defaultValue);
    }

    public int getCellSize(String screenSize) {
        return switch (screenSize) {
            case "small" -> 15;
            case "big" -> 35;
            default -> 25;
        };
    }

    public int getScoreSize(String screenSize) {
        return switch (screenSize) {
            case "small" -> 20;
            case "big" -> 60;
            default -> 40;
        };
    }

    public int getMessageSize(String screenSize) {
        return switch (screenSize) {
            case "small" -> 12;
            case "big" -> 22;
            default -> 17;
        };
    }

    public int getBlockSize(String screenSize) {
        return switch (screenSize) {
            case "small" -> 22;
            case "big" -> 48;
            default -> 36;
        };
    }

    public int getDifficulty() {
        return Integer.parseInt(properties.getProperty("difficulty", "1"));
    }

    public ArrayList<String> getKeys() {
            return KEYS;
    }

    public void setKeys(JTextField[] textField) {
        System.out.println("SETKEYS");
        for (int i = 0; i < textField.length; i++) {
            KEYS.set(i, textField[i].getText());
        }
    }
    public void saveSettings(String key, String value) {
        properties.setProperty(key, value);
        try (FileOutputStream fos = new FileOutputStream(configPath)) {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
        } catch (IOException e) {
            // 파일이 존재하지 않을 경우 초기 설정을 사용할 수 있습니다.
            System.out.println("No existing settings, use default settings.");
        }
    }


    public void initializeSettings() {
        properties.setProperty("screenSize", "small");
        properties.setProperty("colorMode", "default");
        try (FileOutputStream fos = new FileOutputStream(configPath)) {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setColorBlindMode(String mode) {
        saveSettings(COLOR_BLIND_MODE, mode);
    }
}
