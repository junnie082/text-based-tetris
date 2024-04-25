package logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void getPlayerName() {
        Score score = new Score("Player1", 100, "Easy");
        assertEquals("Player1", score.getPlayerName(), "Player name should be 'Player1'.");
    }

    @Test
    void getScore() {
        Score score = new Score("Player1", 100, "Easy");
        assertEquals(100, score.getScore(), "Score should be 100.");
    }

    @Test
    void compareTo() {
        Score score1 = new Score("Player1", 100, "Easy");
        Score score2 = new Score("Player2", 200, "Easy");

        assertTrue(score1.compareTo(score2) > 0, "Score2 should be greater than Score1.");
        assertTrue(score2.compareTo(score1) < 0, "Score1 should be less than Score2.");

        Score score3 = new Score("Player3", 100, "Easy");
        assertEquals(0, score1.compareTo(score3), "Score1 should be equal to Score3 in terms of score.");
    }

    @Test
    void testToString() {
        Score score = new Score("Player1", 100,"Easy");
        assertEquals("Player1:Easy:100", score.toString(), "toString() should return 'Player1:100'.");
    }

    @Test
    void testEquals() {
        Score score1 = new Score("Player1", 100,"Easy");
        Score score2 = new Score("Player1", 100,"Easy");
        Score score3 = new Score("Player2", 200,"Easy");

        assertTrue(score1.equals(score2), "Score1 and Score2 should be equal.");
        assertFalse(score1.equals(score3), "Score1 and Score3 should not be equal.");
    }

    @Test
    void testHashCode() {
        Score score1 = new Score("Player1", 100,"Easy");
        Score score2 = new Score("Player1", 100,"Easy");
        Score score3 = new Score("Player2", 200,"Easy");

        assertEquals(score1.hashCode(), score2.hashCode(), "Hash codes of Score1 and Score2 should be the same.");
        assertNotEquals(score1.hashCode(), score3.hashCode(), "Hash codes of Score1 and Score3 should be different.");
    }
}
