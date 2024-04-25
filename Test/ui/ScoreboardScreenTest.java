package ui;

import logic.Score;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.SocketHandler;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardScreenTest {

    @Test
    void actionPerformed_Menu() {
        // Create Instance
        ScoreboardScreen scoreboardScreen = new ScoreboardScreen();
        scoreboardScreen.setVisible(true);  // 화면에 보이게 설정
        MainMenuScreen mainMenuScreen = new MainMenuScreen();

        // 메뉴 버튼 클릭 이벤트 시뮬레이션
        ActionEvent menuEvent = new ActionEvent(scoreboardScreen.menuBtn, ActionEvent.ACTION_PERFORMED, "menu");
        scoreboardScreen.actionPerformed(menuEvent);

        // 메뉴 버튼 클릭 후 창이 닫혀야 함
        assertFalse(scoreboardScreen.isVisible(), "ScoreboardScreen should be invisible after clicking Menu");
        assertTrue(mainMenuScreen.isVisible(), "MainMenuScreen should become visible after clicking Menu");
    }

    @Test
    void actionPerformed_Game() {
        // Create Instance
        Score newScore = new Score("Player1", 100,"Easy");
        ScoreboardScreen scoreboardScreen = new ScoreboardScreen(newScore, false);
        scoreboardScreen.setVisible(true);  // 화면에 보이게 설정
        MainMenuScreen mainMenuScreen = new MainMenuScreen();

        // 메뉴 버튼 클릭 이벤트 시뮬레이션
        ActionEvent menuEvent = new ActionEvent(scoreboardScreen.menuBtn, ActionEvent.ACTION_PERFORMED, "menu");
        scoreboardScreen.actionPerformed(menuEvent);

        // 메뉴 버튼 클릭 후 창이 닫혀야 함
        assertFalse(scoreboardScreen.isVisible(), "ScoreboardScreen should be invisible after clicking Menu");
        assertTrue(mainMenuScreen.isVisible(), "MainMenuScreen should become visible after clicking Menu");
    }


    @Test
    void keyListener_Menu() {
        // Create Instance
        ScoreboardScreen scoreboardScreen = new ScoreboardScreen();
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        scoreboardScreen.setVisible(true);  // 화면에 보이게 설정
        scoreboardScreen.menuBtn.requestFocus();  // 메뉴 버튼에 포커스 설정

        // 엔터 키 이벤트 시뮬레이션
        KeyEvent enterEvent = new KeyEvent(scoreboardScreen.menuBtn, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
        scoreboardScreen.menuBtn.dispatchEvent(enterEvent);

        // 엔터 키 입력 후 창이 닫혀야 함
        assertFalse(scoreboardScreen.isVisible(), "ScoreboardScreen should be invisible after pressing Enter");
        assertTrue(mainMenuScreen.isVisible(), "MainMenuScreen should become visible after clicking Menu");
    }

    @Test
    void keyListener_Game() {
        // Create Instance
        Score newScore = new Score("Player1", 100,"Easy");
        ScoreboardScreen scoreboardScreen = new ScoreboardScreen(newScore, false);
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        scoreboardScreen.setVisible(true);  // 화면에 보이게 설정
        scoreboardScreen.menuBtn.requestFocus();  // 메뉴 버튼에 포커스 설정

        // 엔터 키 이벤트 시뮬레이션
        KeyEvent enterEvent = new KeyEvent(scoreboardScreen.menuBtn, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
        scoreboardScreen.menuBtn.dispatchEvent(enterEvent);

        // 엔터 키 입력 후 창이 닫혀야 함
        assertFalse(scoreboardScreen.isVisible(), "ScoreboardScreen should be invisible after pressing Enter");
        assertTrue(mainMenuScreen.isVisible(), "MainMenuScreen should become visible after clicking Menu");
    }
}