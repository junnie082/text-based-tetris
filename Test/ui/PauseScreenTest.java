package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class PauseScreenTest {

    private PauseScreen pauseScreen;
    private Robot robot;
    private boolean gameResumed;
    private boolean frameHidden;

    @BeforeEach
    void setUp() throws AWTException {
        pauseScreen = new PauseScreen(false);
        robot = new Robot();
        robot.setAutoDelay(100);

        pauseScreen.setCallback(new PauseScreenCallback() {
            @Override
            public void onResumeGame() {
                gameResumed = true;
            }

            @Override
            public void onHideFrame() {
                frameHidden = true;
            }
        });
        pauseScreen.setVisible(true);
    }

    @Test
    void testActions() {
        pauseScreen.btnBack.doClick();
        assertTrue(gameResumed, "Game should resume on 'Back' button click");

        pauseScreen.btnReplay.doClick();
        assertTrue(frameHidden, "'Frame should be hidden on 'Replay' button click");

        pauseScreen.btnMainMenu.doClick();
        assertTrue(frameHidden, "'Frame should be hidden on 'MainMenu' button click");
    }

    @Test
    void testFocusMovement() throws  InterruptedException {
        pauseScreen.btnBack.requestFocus();
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(pauseScreen.btnReplay.isFocusOwner(), "Focus should move to the Replay button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(pauseScreen.btnMainMenu.isFocusOwner(), "Focus should move to the Menu button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        assertTrue(pauseScreen.btnQuit.isFocusOwner(), "Focus should move to the Quit button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        assertTrue(pauseScreen.btnMainMenu.isFocusOwner(), "Focus should move to the Menu button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        assertTrue(pauseScreen.btnReplay.isFocusOwner(), "Focus should move to the Replay button");

        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        assertTrue(pauseScreen.btnBack.isFocusOwner(), "Focus should move to the Back to the Game button");
    }
}
