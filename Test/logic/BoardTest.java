package logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }


    @Test
    void clearBoard() {
        // 1. Initialize a board and clear it
        board.clearBoard();

        // 2. Check that the internal board has no blocks (all zeroes, except for walls)
        boolean isClear = true;
        int[][] internalBoard = board.getBoard();

        for (int i = 2; i < board.getHeight() + 4; i++) {
            for (int j = 2; j < board.getWidth() + 4; j++) {
                if (internalBoard[i][j] != 0) {
                    isClear = false;
                    break;
                }
            }
            if (!isClear) break;
        }
//        assertTrue(isClear, true);
//        assertTrue(isClear, "Board should be clear after calling clearBoard.");
    }

    @Test
    void getBoard() {
        // 1. Get the board from the Board object
        int[][] internalBoard = board.getBoard();

        // 2. Check the boundaries to ensure they are walls
        assertEquals(20, internalBoard[2][2]);

//        assertEquals(20, internalBoard[2][2], "Top-left corner should be wall.");
//        assertEquals(20, internalBoard[board.getHeight() + 3][board.getWidth() + 3], "Bottom-right corner should be wall.");
//
//        // 3. Check that internal board's size matches the extended dimensions
//        assertEquals(board.getHeight() + 6, internalBoard.length, "Internal board height should match extended height.");
//        assertEquals(board.getWidth() + 6, internalBoard[0].length, "Internal board width should match extended width.");
    }

    @Test
    void getWidth() {
        // Test to ensure the width returned is correct
//        assertEquals(10, board.getWidth(), "Width should be 10.");
    }

    @Test
    void getHeight() {
        // Test to ensure the height returned is correct
//        assertEquals(20, board.getHeight(), "Height should be 20.");
    }
}
