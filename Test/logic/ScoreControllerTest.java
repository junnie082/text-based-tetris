package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


class ScoreControllerTest {
    private ScoreController scoreController;

    @BeforeEach
    void setUp() {
        scoreController = new ScoreController();
        scoreController.resetScores(false); // reset normal scores
        scoreController.resetScores(true); // reset item scores
    }

    @Test
    void addScore() {
        // Add a score to the normal scores
        Score score1 = new Score("Player1", 100, "Easy");
        scoreController.addScore(score1, false);

        // Ensure the score was added to the normal scores
        List<Score> normalScores = scoreController.getScores(false);
        assertFalse(normalScores.isEmpty(), "Normal scores should not be empty after adding a score.");
        assertEquals("Player1:Easy:100", normalScores.get(0).toString(), "Added score should match the input.");

        // Add a score to the item scores
        Score score2 = new Score("Player1", 100, "Easy");
        scoreController.addScore(score2, true);

        // Ensure the score was added to the item scores
        List<Score> itemScores = scoreController.getScores(true);
        assertFalse(itemScores.isEmpty(), "Item scores should not be empty after adding a score.");
        assertEquals("Player1:Easy:100", itemScores.get(0).toString(), "Added score should match the input.");
    }

    @Test
    void addScore_largerThen10(){
        // Add scores to create a Top 10 list
        for (int i = 0; i < 10; i++) {
            Score score = new Score("Player" + (i + 1), i * 10, "Easy");
            scoreController.addScore(score, false);
        }

        // Add a score that would be in the Top 10
        Score score11 = new Score("Player11", 100, "Easy");
        scoreController.addScore(score11, false);

        // Ensure the score was added to the normal scores
        List<Score> normalScores = scoreController.getScores(false);
        assertEquals(10, normalScores.size(), "Normal scores should have 10 items.");
        assertEquals("Player11:Easy:100", normalScores.get(0).toString(), "Added score should be at the top.");

        // Add a score that would not be in the Top 10
        Score score12 = new Score("Player12", 5, "Easy");
        scoreController.addScore(score12, false);

        // Ensure the score was not added to the normal scores
        assertEquals(10, normalScores.size(), "Normal scores should still have 10 items.");
    }

    @Test
    void resetScores() {
        // Add some scores
        Score score1 = new Score("Player1", 100, "Easy");
        Score score2 = new Score("Player1", 200, "Easy");
        scoreController.addScore(score1, false);
        scoreController.addScore(score2, true);

        // Reset the normal scores and ensure it's empty
        scoreController.resetScores(false);
        List<Score> normalScores = scoreController.getScores(false);
        assertTrue(normalScores.isEmpty(), "Normal scores should be empty after reset.");

        // Reset the item scores and ensure it's empty
        scoreController.resetScores(true);
        List<Score> itemScores = scoreController.getScores(true);
        assertTrue(itemScores.isEmpty(), "Item scores should be empty after reset.");
    }

    @Test
    void getScores() {
        // Add scores to both normal and item lists
        Score score1 = new Score("Player1", 100, "Easy");
        Score score2 = new Score("Player1", 200, "Easy");
        scoreController.addScore(score1, false);
        scoreController.addScore(score2, true);

        // Get the scores and check if they are correct
        List<Score> normalScores = scoreController.getScores(false);
        List<Score> itemScores = scoreController.getScores(true);

        assertEquals(1, normalScores.size(), "Normal scores should have 1 item.");
        assertEquals(1, itemScores.size(), "Item scores should have 1 item.");
    }

    @Test
    void isScoreInTop10() {
        // Add scores to create a Top 10 list
        for (int i = 0; i < 10; i++) {
            Score score = new Score("Player" + (i + 1), i * 10, "Easy");
            scoreController.addScore(score, false);
        }

        // Check if a score would be in the Top 10
        assertTrue(scoreController.isScoreInTop10(100, false), "100 should be in the Top 10.");
        assertFalse(scoreController.isScoreInTop10(0, false), "0 should not be in the Top 10.");
    }

    @Test
    void isScoreInTop10_withLessThen10Scores(){
        // Check if a score would be in the Top 10 when there are less than 10 scores
        assertTrue(scoreController.isScoreInTop10(100, false), "100 should be in the Top 10.");
    }
}
