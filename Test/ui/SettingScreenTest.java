package ui;

import logic.SettingController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class SettingScreenTest {

    private SettingScreen settingScreen;
    private SettingController settingController;
    private Robot robot;


    @BeforeEach
    void setUp() throws AWTException {
        settingScreen = new SettingScreen();
        settingController = settingScreen.getSettingController();
        robot = new Robot();
        robot.setAutoDelay(100);
    }

    @Test
    void actionPerformedTest() {

        // Simulate clicking the "Small" button
        ActionEvent smallEvent = new ActionEvent(settingScreen.btnSize1, ActionEvent.ACTION_PERFORMED, "small");
        settingScreen.actionPerformed(smallEvent);
        assertEquals("small", settingController.getScreenSize("screenSize", "medium"));

        // Simulate clicking the "Medium" button
        ActionEvent mediumEvent = new ActionEvent(settingScreen.btnSize2, ActionEvent.ACTION_PERFORMED, "medium");
        settingScreen.actionPerformed(mediumEvent);
        assertEquals("medium", settingController.getScreenSize("screenSize", "medium"));

        // Simulate clicking the "Big" button
        ActionEvent bigEvent = new ActionEvent(settingScreen.btnSize3, ActionEvent.ACTION_PERFORMED, "big");
        settingScreen.actionPerformed(bigEvent);
        assertEquals("big", settingController.getScreenSize("screenSize", "medium"));

        // Simulate clicking the "Key Setting" button
        KeySettingScreen keySettingScreen = new KeySettingScreen();
        ActionEvent keySettingEvent = new ActionEvent(settingScreen.btnKeySetting, ActionEvent.ACTION_PERFORMED, "keySetting");
        settingScreen.actionPerformed(keySettingEvent);
        assertFalse(settingScreen.isVisible(), "SettingScreen should be invisible after opening Key Settings");
        assertTrue(keySettingScreen.isVisible(), "KeySettingScreen should become visible after clicking Key Settings");

        // Simulate clicking the "Initialize Key Setting" button
        ActionEvent initializeKeyEvent = new ActionEvent(settingScreen.btnInitializeKeySetting, ActionEvent.ACTION_PERFORMED, "initializeKey");
        settingScreen.actionPerformed(initializeKeyEvent);
        assertArrayEquals(new int[]{38, 37, 39, 40, 32}, settingController.getKeyCodes(), "Key codes should be reset to defaults");

        // Simulate clicking the "Default Colorblind Mode" button
        ActionEvent colorBlind0Event = new ActionEvent(settingScreen.btnColorBlind0, ActionEvent.ACTION_PERFORMED, "default");
        settingScreen.actionPerformed(colorBlind0Event);
        assertEquals("default", settingController.getColorMode("colorBlindMode", "default"));

        // Simulate clicking the "Default Colorblind Mode" button
        ActionEvent colorBlind1Event = new ActionEvent(settingScreen.btnColorBlind1, ActionEvent.ACTION_PERFORMED, "colorBlind1");
        settingScreen.actionPerformed(colorBlind1Event);
        assertEquals("protanopia", settingController.getColorMode("colorBlindMode", "default"));

        // Simulate clicking the "Default Colorblind Mode" button
        ActionEvent colorBlind2Event = new ActionEvent(settingScreen.btnColorBlind2, ActionEvent.ACTION_PERFORMED, "colorBlind2");
        settingScreen.actionPerformed(colorBlind2Event);
        assertEquals("deuteranopia", settingController.getColorMode("colorBlindMode", "default"));

        // Simulate clicking the "Default Colorblind Mode" button
        ActionEvent colorBlind3Event = new ActionEvent(settingScreen.btnColorBlind3, ActionEvent.ACTION_PERFORMED, "colorBlind3");
        settingScreen.actionPerformed(colorBlind3Event);
        assertEquals("tritanopia", settingController.getColorMode("colorBlindMode", "default"));

        // Simulate clicking the "Menu" button
        ActionEvent event = new ActionEvent(settingScreen.btnMenu, ActionEvent.ACTION_PERFORMED, "menu");
        settingScreen.actionPerformed(event);
        assertFalse(settingScreen.isVisible(), "SettingScreen should be invisible after clicking Menu");
    }

    @Test
    void testFocusMovementRight() throws InterruptedException {
        settingScreen.btnSize1.requestFocus();
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(settingScreen.btnSize2.isFocusOwner(), "Focus should move to the medium size button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(settingScreen.btnSize3.isFocusOwner(), "Focus should move to the big size button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        assertTrue(settingScreen.btnKeySetting.isFocusOwner(), "Focus should move to the key setting button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(settingScreen.btnInitializeKeySetting.isFocusOwner(), "Focus should move to the initialize key setting button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        assertTrue(settingScreen.btnInitializeNormalScore.isFocusOwner(), "Focus should move to the initialize normal score button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(settingScreen.btnInitializeItemScore.isFocusOwner(), "Focus should move to the initialize item score button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        assertTrue(settingScreen.btnColorBlind1.isFocusOwner(), "Focus should move to the colorblind - 1 button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(settingScreen.btnColorBlind2.isFocusOwner(), "Focus should move to the colorblind - 2 button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(settingScreen.btnColorBlind3.isFocusOwner(), "Focus should move to the colorblind - 3 button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        assertTrue(settingScreen.btnInitializeSetting.isFocusOwner(), "Focus should move to the initialize setting button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        assertTrue(settingScreen.btnMenu.isFocusOwner(), "Focus should move to the back to the menu button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        assertTrue(settingScreen.btnInitializeSetting.isFocusOwner(), "Focus should move to the initialize setting button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        assertTrue(settingScreen.btnColorBlind2.isFocusOwner(), "Focus should move to the colorblind - 2 button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        assertTrue(settingScreen.btnColorBlind1.isFocusOwner(), "Focus should move to the colorblind - 1 button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        assertTrue(settingScreen.btnColorBlind0.isFocusOwner(), "Focus should move to the colorblind - 0 button");
    }
}
