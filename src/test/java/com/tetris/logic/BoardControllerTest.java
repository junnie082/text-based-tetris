package com.tetris.logic;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardControllerTest {
    boolean isItemMode;
    GameController gameController;
    BoardController boardController;
    InGameScoreController inGameScoreController;

    @BeforeEach
    void setUp() {
        isItemMode = true;
        gameController = new GameController(isItemMode);
        boardController = new BoardController(gameController, inGameScoreController, isItemMode, false);
    }

    @Test
    @DisplayName("새로운 블록을 (6, 2) 좌표에 생성한다.")
    void placeNewBlock() {
        // given
        int placedBlockCount = boardController.getPlacedBlockCount();
        Block nextBlock = boardController.getNextBlock();

        // when
        boardController.placeNewBlock();

        // then
        // current block이 next block이 된다.
        assertEquals(boardController.getCurrentBlock(), nextBlock);
        // 새로운 next block이 생성된다.
        Assertions.assertNotNull(boardController.getNextBlock());
        // placedBlockCount가 1증가한다.
        Assertions.assertTrue(boardController.getPlacedBlockCount() == placedBlockCount + 1);
        // 현재 블록의 위치가 (6, 2)가 된다.
        Assertions.assertTrue(boardController.getX() == 6);
        Assertions.assertTrue(boardController.getY() == 2);
    }

    @Test
    @DisplayName("충돌을 검사하고, 충돌하지 않으면 true를 반환한다.")
    void collisionCheck() {
        // given
        int newX = 13;
        int newY = 23;

        // 이미 블록이 놓여져 있을 때,
        boardController.placeNewBlock();

        // when
        boolean result = boardController.collisionCheck(newX, newY);

        // then
        Assertions.assertFalse(result);


    }

    @Test
    @DisplayName("라인이 꽉 찼는지 확인하고, 꽉 찼으면 지운다.")
    void lineCheck() {
        // given
        int[][] board = boardController.getBoard();
        int row = 5;
        // row번째 줄이 모두 채워졌을 때,
        for(int i = 3; i <= 13; ++i) board[row][i] = 1;
        int score = inGameScoreController.getScore();

        // when
        boardController.lineCheck();

        // blinkLine()이 호출되어, 해당 줄이 -2으로 변한다.
        assertEquals(-2, board[row][3]);
        // 라인이 지워지면 점수가 올라간다.
        assertTrue(score < inGameScoreController.getScore());

    }

    @Test
    @DisplayName("줄 삭제 아이템이 있는 경우에 해당 줄을 삭제한다.")
    void lineCheckWithLineDeletionItem() {
        // given
        int[][] board = boardController.getBoard();
        int row = 5;
        int column = 7;
        // row, column 좌표에 줄 삭제 아이텤이 있는 경우
        board[row][column] = 8;
        int score = inGameScoreController.getScore();

        // when
        boardController.lineCheck();

        // blinkLine()이 호출되어, 해당 줄이 -2으로 변한다.
        assertEquals(-2, board[row][column]);
        // 라인이 지워지면 점수가 올라간다.
        assertTrue(score < inGameScoreController.getScore());

    }

    @Test
    @DisplayName("블럭이 10번 생성될 때마다 지워진 줄이 없으면 50점 감소한다.")
    void lineCheckInTenTimes() {
        // Given: Set initial score
        int initialScore = inGameScoreController.getScore();

        // When: No lines are deleted across 10 block placements
        for (int i = 0; i < 10; i++) {
            boardController.lineCheck();  // Simulate line check with no line deletion
            inGameScoreController.subScoreOnLineNotEraseIn10Blocks(); // Manually trigger scoring rule
        }

        // Then: Verify no score change after the first 10 checks
        assertEquals(initialScore, inGameScoreController.getScore(),"Score should not change after 10 block placements with no line deletions");

        // Another lineCheck to trigger the score decrement
        boardController.lineCheck();  // No lines will be deleted
        inGameScoreController.subScoreOnLineNotEraseIn10Blocks(); // This should now decrease the score

        // Expect: Score should be decreased by 50 after the 11th block
        assertEquals(initialScore - 50, inGameScoreController.getScore(), "Score should decrease by 50 after the 11th block placement with no line deletion");
    }

    @Test
    @DisplayName("블록을 회전시키고, 충돌 시 회전하지 않는다.")
    void rotateBlock() {


    }

}
