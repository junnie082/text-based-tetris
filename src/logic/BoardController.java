package logic;

import model.*;
import ui.InGameScreen;

import java.util.LinkedList;
import java.util.Queue;

public class BoardController {
    public static final int BOMB_BODY = 11;
    public static final int BOMB_EVENT = 13;
    public static final int EXTEND_BLOCK = 14;
    public static final int EXTEND_BLOCK_EVENT = 15;
    // 게임 보드
    boolean waterBlockMoved = false;
    final private Board grid;
    // 게임 보드의 너비, 높이
    final private int WIDTH;
    final private int HEIGHT;
    private int erasedLineCount = 0;
    private boolean isItemMode;
    // 현재 블록, 다음 블록
    private Block currentBlock;
    private Block nextBlock = new NullBlock();
    // stopCount: 조작이 없으면 1씩 증가,
    private int stopCount = 0;
    // limitCount: 블록이 바닥에 닿은 순간 1씩 증가, 2 초과시 블록을 고정.
    // 블록이 moveDown => 0으로 초기화
    private int limitCount = 0;

    private boolean canPlaceBlock;

    private int score;

    private long lastLineEraseTime;

    private int blockCountWithNoLineErase;

    private int placedBlockCount;
    private boolean canMoveSide;

    final int BLOCK_COUNT_TO_SPEED_UP = 15;
    final int LINE_COUNT_TO_SPEED_UP = 8;
    final int BLOCK_SPEED_UP_THRESHOLD = 20;
    final int LINE_SPEED_UP_THRESHOLD = 40;

    private boolean needNewBlock = false;

    public boolean getNewBlockState() {
        return needNewBlock;
    }

    private void setNewBlockState(boolean state) {
        needNewBlock=state;
        if(state){
            excludeBomb();
            extendBlocks();
            flowWaterBlock();
        }
    }

    // 블록의 초기 좌표
    int x, y;

    private void checkSpeedUp() {

        if (placedBlockCount >= BLOCK_COUNT_TO_SPEED_UP) {
            placedBlockCount = 0;

            gameController.speedUp(BLOCK_SPEED_UP_THRESHOLD);
        }
        if (erasedLineCount % LINE_COUNT_TO_SPEED_UP == 0 && erasedLineCount != 0) {
            gameController.speedUp(LINE_SPEED_UP_THRESHOLD);
        }
    }

    private final Queue<String> Messages;


    GameController gameController;
    InGameScreen inGameScreen;


    public String getScoreMessages() {
        StringBuilder sb = new StringBuilder();
        for (String message : Messages) {
            sb.append(message).append("\n");
        }
        return sb.toString();
    }


    public BoardController(GameController gameController, Boolean isItemMode) {
        this.isItemMode = isItemMode;
        this.grid = new Board();
        this.WIDTH = grid.getWidth();
        this.HEIGHT = grid.getHeight();
        this.score = 0;
        this.canPlaceBlock = true;
        this.blockCountWithNoLineErase = 0;
        this.Messages = new LinkedList<>();
        this.gameController = gameController;
        this.lastLineEraseTime = 0;
        this.canMoveSide = true;
        this.nextBlock = nextBlock.selectBlock(isItemMode, erasedLineCount);
    }

    // 점수 추가
    private void addScore(int score) {
        this.score += score;
    }

    // 블록 이동 시 점수 추가
    private void addScoreOnBlockMoveDown() {
        addScore(1);
    }

    private void addScoreOnBlockMoveDown(int changedY) {
        addScore(changedY);
    }


    // 라인 삭제 시 점수 추가, 한번에 여러 라인 삭제 시 추가 점수, 3초 안에 라인 삭제 시 추가 점수
    private void addScoreOnLineEraseWithBonus(int lineCount) {
        long currLineEraseTime = System.currentTimeMillis();
        long diff = currLineEraseTime - lastLineEraseTime;
        lastLineEraseTime = currLineEraseTime;
        int multiplier = 1;
        if (diff < 3000) {
            multiplier = 2;
        }
        int addedScore = (13 * lineCount - 3) * multiplier;
        addScore(addedScore);
        if (multiplier == 2) {
            if (lineCount == 1) {
                addScoreMessage("+" + addedScore + ": Line erased (x2)");
            } else {
                addScoreMessage("+" + addedScore + ": Lines erased (x2)");
            }
        } else {
            if (lineCount == 1) {
                addScoreMessage("+" + addedScore + ": Line erased");
            } else {
                addScoreMessage("+" + addedScore + ": Lines erased");
            }
        }
    }

    private void subScoreOnLineNotEraseIn10Blocks() {
        if (blockCountWithNoLineErase > 10) {
            addScore(-50);
            blockCountWithNoLineErase = 0;
            addScoreMessage("-50: No line erased in 10 blocks");
        }
    }

    public void addScoreMessage(String message) {
        if (Messages.size() > 7) {
            Messages.remove();
        }
        Messages.add(message);
    }

    // InGameScreen 에서 다음 블록 띄우기 위해서 추가
    public Block getNextBlock() {
        return nextBlock;
    }

    // InGameScreen 에서 게임 보드 상태를 가져오기 위해서 추가
    public int[][] getBoard() {
        return grid.getBoard();
    }


    // nextBlock을 currentBlock으로 옮기고 새로운 nextBlock을 생성
    public void placeNewBlock() {
        placedBlockCount++;
        blockCountWithNoLineErase++;
        this.currentBlock = nextBlock;
        canMoveSide = true;
        this.nextBlock = nextBlock.selectBlock(isItemMode, erasedLineCount);
        if (collisionCheck(6, 2)) {
            checkSpeedUp();
            x = 6;
            y = 2;
        } else {
            canPlaceBlock = false;
            this.currentBlock = new NullBlock();
        }
        setNewBlockState(false);
        // addScoreMessage(Block.getErasedLineCountForItem() + "");
    }


    // 블록을 게임 보드에 배치
    public void placeBlock() {
        // 기본 블록 배치 로직
        for (int j = 0; j < currentBlock.height(); j++) {
            for (int i = 0; i < currentBlock.width(); i++) {
                grid.getBoard()[y + j][x + i] += currentBlock.getShape(i, j);
            }
        }
    }


    // 충돌 검사, 충돌하지 않으면 true 반환
    public boolean collisionCheck(int newX, int newY) {
        for (int i = 0; i < currentBlock.height(); i++) {
            for (int j = 0; j < currentBlock.width(); j++) {
                if (currentBlock.getShape(j, i) != 0) { // Check if part of the block
                    int boardX = newX + j;
                    int boardY = newY + i;
                    if (boardX < 3 || boardX >= WIDTH + 3 || boardY < 3 || boardY >= HEIGHT + 3) {
                        return false; // Out of bounds
                    }
                    if (grid.getBoard()[boardY][boardX] != 0) {
                        // F((WeightItemBlock)currentBlock).isActive = false;
                        return false; // Position already occupied
                    }
                }
            }
        }
        return true; // No collision detected
    }


    // 라인이 꽉 찼는지 확인하고 꽉 찼으면 지우기
    public void lineCheck() {
        int lineCount = 0;
        for (int i = 3; i < HEIGHT + 3; i++) {
            boolean canErase = true;
            for (int j = 3; j < WIDTH + 3; j++) {
                // 블럭이 배치되어 있지 않거나 애니메이션 중인 블럭인 경우
                if (grid.getBoard()[i][j] == 0 || grid.getBoard()[i][j] == -2
                        // 폭탄 블럭은 라인체크에 반영 X
                        || grid.getBoard()[i][j] == BOMB_BODY || grid.getBoard()[i][j] == BOMB_EVENT) {
                    canErase = false;
                }
                if (grid.getBoard()[i][j] == 8) {
                    // 줄 삭제 아이템이 있는 경우
                    canErase = true;
                    addScoreMessage("Event: Line Erased");
                    break;
                }
            }
            if (canErase) {
                blinkLine(i);
                lineCount++;
                erasedLineCount++;
            }
        }
        if (lineCount > 0) {
            blockCountWithNoLineErase = 0;
            addScoreOnLineEraseWithBonus(lineCount);
        } else {
            subScoreOnLineNotEraseIn10Blocks();
        }
    }


    // 라인을 지우고 위에 있는 블록들을 내림
    public void eraseLine(int line) {
        for(int i=3; i<WIDTH+3; i++) {
            grid.getBoard()[line][i] = 0;
        }
        for (int i = line; i > 3; i--) {
            System.arraycopy(grid.getBoard()[i - 1], 3, grid.getBoard()[i], 3, WIDTH);
        }
    }


    // 현재 배열에서 블록을 지움, 블록을 회전하거나 이동하기 전에 사용
    private void eraseCurrentBlock() {
        for (int i = 0; i < currentBlock.width(); i++) {
            for (int j = 0; j < currentBlock.height(); j++) {
                if (grid.getBoard()[y + j][x + i] - currentBlock.getShape(i, j) >= 0) {
                    grid.getBoard()[y + j][x + i] -= currentBlock.getShape(i, j);
                }
            }
        }
    }

    private void eraseOneBlock(int x, int y) {
        grid.getBoard()[y][x] = 0;
    }

    private void placeOneBlock(int x, int y, int blockType) {
        grid.getBoard()[y][x] = blockType;
    }

    private boolean collisionCheckForOneBlock(int x, int y) {
        if(grid.getBoard()[y][x] != 0) {
            return false;
        }
        return true;
    }

    // 블록을 이동시킴
    public void moveBlock(Direction direction) {
        if (needNewBlock) {
            return;
        }
        eraseCurrentBlock();
        if (!canPlaceBlock) {
            return;
        }
        switch (direction) {
            case LEFT -> {
                if (canMoveSide && collisionCheck(x - 1, y)) {
                    x--;
                    stopCount = 0;
                }
                placeBlock();
            }
            case RIGHT -> {
                if (canMoveSide && collisionCheck(x + 1, y)) {
                    x++;
                    stopCount = 0;
                }
                placeBlock();
            }
            case DOWN -> {
                if (collisionCheck(x, y + 1)) {
                    placeDown();
                    addScoreOnBlockMoveDown(); // 한 칸 내릴 때마다 1점 추가
                } else { // 충돌!
                    // 무게추 블럭인 경우
                    if (currentBlock instanceof WeightItemBlock) {
                        canMoveSide = false;
                        breakBlocks(x, y + currentBlock.height() - 1, currentBlock.width());
                    } else {
                        // 무게추 블럭이 아닌 경우
                        stopCount++;
                        limitCount++;
                    }
                    placeBlock();

                    // 2틱 동안 움직임 없거나 충돌 후 2틱이 지나면 블록을 고정시킴
                    if (stopCount > 1 || limitCount > 1) {
                        checkGameOver();
                        setNewBlockState(true);
                        lineCheck();
                        stopCount = 0;
                        limitCount = 0;
                    }
                }
            }
            case UP -> {
                eraseCurrentBlock();
                rotateBlock();
                placeBlock();
            }
            case SPACE -> {

                // space바 누르면 바로 터지게
                if (currentBlock instanceof BombItemBlock) {
                    placeBlock();
                    setNewBlockState(true);
                    break;
                }

                while (collisionCheck(x, y + 1)) {
                    y++;
                    addScoreOnBlockMoveDown(); // 한 칸 내릴 때마다 1점 추가
                }
                placeBlock();

                // 무게 추 블럭인 경우 새로운 다음 블럭을 불러오면 안됌
                if (currentBlock instanceof WeightItemBlock) {
                    break;
                }

                setNewBlockState(true);
                lineCheck();

                limitCount = 0;
            }
            default -> placeBlock();
        }
    }

    // 블록을 회전시킴. 충돌 시 회전하지 않음
    public void rotateBlock() {
        currentBlock.rotate();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (collisionCheck(x + i, y + j)) {
                    addScoreOnBlockMoveDown(j);
                    x += i;
                    y += j;
                    return;
                }
                if (collisionCheck(x - i, y + j)) {
                    addScoreOnBlockMoveDown(j);
                    x -= i;
                    y += j;
                    return;
                }
            }
        }
        currentBlock.rotateBack();
    }

    // Debug: 게임 보드 출력
    public void printBoard() {
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(grid.getBoard()[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Game Over Check
    // TODO: 3/24/24 : 게임 오버 조건 수정 확인 필요, ScoreController에게 점수 전달 로직 추가 필요
    public boolean checkGameOver() {

        for (int i = 3; i < WIDTH + 3; i++) {
            if ((grid.getBoard()[2][i] > 20) || !canPlaceBlock) {

                return true;
            }
        }
        return false;
    }


    public int getScore() {
        return score;
    }

    private void placeDown() {
        stopCount = 0;
        limitCount = 0;
        y++;
        placeBlock();
    }

    // ToDo: 줄 삭제 이벤트 로직
    private void blinkLine(int line) {
        for (int i = 3; i < WIDTH + 3; i++) {
            grid.getBoard()[line][i] = -2;
        }
    }

    public boolean blinkCheck() {
        boolean blink = false;

        for (int i = 3; i < HEIGHT + 3; i++) {
            if (grid.getBoard()[i][3] == -2) {
                eraseLine(i);
                blink = true;
            }

            // 폭탄 이벤트 제거
            for (int j = 3; j < WIDTH + 3; j++) {
                if (grid.getBoard()[i][j] == 13) {
                    eraseOneBlock(j, i);
                }
            }
        }

        return blink;
    }


    // ToDo: 무게 추 블럭 로직

    private void breakBlocks(int x, int y, int length) {
        // 경계선에 닿은 경우
        if (x <= 2 || x >= WIDTH + 3 || y <= 2 || y >= HEIGHT + 2) {
            stopCount = 2;
            limitCount = 2;
            return;
        }

        for (int i = x; i < x + length; i++) {
            grid.getBoard()[y + 1][i] = 0;
        }

        for (int i = y; i > 3; i--) {
            System.arraycopy(grid.getBoard()[i - 1], x, grid.getBoard()[i], x, length);
        }

        this.y += 1;
    }


    // TODO: 물 블럭 로직

    //물 블록 좌우 흐름 가능 여부
    private boolean canFlowSide(int x, int y, Direction direction) {
        // 현재 위치에서 방향을 따른 옆 칸이 보드 안에 있는지 확인
        if (direction == Direction.LEFT && (x - 1 >= 3)) {
            return grid.getBoard()[y][x - 1] == 0;  // 왼쪽으로 확장 가능한지 확인
        }
        else if (direction == Direction.RIGHT && (x + 1 <= 12)) {
            return grid.getBoard()[y][x + 1] == 0;  // 오른쪽으로 확장 가능한지 확인
        }
        return false;
    }

    //물 블록 좌우 흐름
    private void flowSide(int x, int y) {
        int newX = x;
        while (grid.getBoard()[y][newX + 1] == 10){
            newX++;
        }
        if(canFlowSide(newX, y, Direction.RIGHT)) {
            for(int j=newX; j>=x; j--) {
                eraseOneBlock(j, y);
                placeOneBlock(j + 1, y, 10);
            }
            eraseOneBlock(x, y-1);
            placeOneBlock(x, y, 10);
            waterBlockMoved = true;
            return;
        }
        newX = x;
        while (grid.getBoard()[y][newX - 1] == 10){
            newX--;
        }
        if(canFlowSide(newX, y, Direction.LEFT)) {
            for(int j=newX; j<=x; j++) {
                eraseOneBlock(j, y);
                placeOneBlock(j - 1, y, 10);
            }
            eraseOneBlock(x, y-1);
            placeOneBlock(x, y, 10);
            waterBlockMoved = true;
        }
    }

    //물 블록 흐름
    public void flowWaterBlock() {
        do {
            waterBlockMoved = false;
            for (int height = 3; height < 23; height++) {
                for (int width = 3; width < 13; width++) {
                    if (grid.getBoard()[height][width] == 10) {  // 물 블록 발견
                        // 아래로 흐를 수 있는지 확인
                        if (grid.getBoard()[height + 1][width] == 0) {
                            eraseOneBlock(width, height);
                            placeOneBlock(width, height + 1, 10);
                            waterBlockMoved = true;
                        } else {
                            if (grid.getBoard()[height + 1][width] == 10) {
                                // 옆으로 흐를 수 있는지 확인
                                flowSide(width, height + 1);
                            }
                        }
                    }
                }
            }
        } while (waterBlockMoved);  // 블록이 이동하는 동안 계속 반복
        lineCheck();
    }


    // ToDo: 폭탄 블럭 로직
    private void excludeBomb() {
        // 폭탄 블록 주변 8칸을 지우기 위한 방향 배열
        int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] dy = {-1, -1, 0, 1, 1, 1, 0, -1};

        // 상하 좌우로 한 칸씩 더 지우기 위한 방향 배열
        int[] dx2 = {0, 1, 2, 1, 0, -1, -2, -1};
        int[] dy2 = {-2, -1, 0, 1, 2, 1, 0, -1};

        boolean[][] toRemove = new boolean[26][16];
        for (int height = 3; height < 23; height++) {
            for (int width = 3; width < 13; width++) {

                if (grid.getBoard()[height][width] == BOMB_BODY) {  // 폭탄 블록 발견
                    toRemove[height][width] = true;
                    for(int i = 0; i < 8; ++i) {
                        int nx = width + dx[i];
                        int ny = height + dy[i];
                        if(nx >= 3 && nx < 13 && ny >= 3 && ny < 23) {
                            toRemove[ny][nx] = true;
                        }

                        // 상하좌우로 한 칸을 더 탐색하여 블럭 지우기
                        int nx2 = width + dx2[i];
                        int ny2 = height + dy2[i];
                        if(nx2 >= 3 && nx2 < 13 && ny2 >= 3 && ny2 < 23) {
                            toRemove[ny2][nx2] = true;
                        }
                    }
                }
            }
        }

        for (int height = 3; height < 23; height++) {
            for (int width = 3; width < 13; width++) {
                if (toRemove[height][width]) {  // 폭탄 블록 발견
                    grid.getBoard()[height][width] = 13;
                    // eraseOneBlock(width, height);
                }
            }
        }
    }

    // ToDo: 확장 블럭 로직
    private void extendBlocks() {
        int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] dy = {-1, -1, 0, 1, 1, 1, 0, -1};
        boolean[][] toCreate = new boolean[26][16];
        for (int height = 3; height < 23; height++) {
            for (int width = 3; width < 13; width++) {

                if (grid.getBoard()[height][width] == EXTEND_BLOCK) {  // 확장 아이템 블록 발견
                    toCreate[height][width] = true;
                    for(int i = 0; i < 8; ++i) {
                        int nx = width + dx[i];
                        int ny = height + dy[i];
                        if(nx >= 3 && nx < 13 && ny >= 3 && ny < 23) {
                            toCreate[ny][nx] = true;
                        }
                    }
                }
            }
        }

        for (int height = 3; height < 23; height++) {
            for (int width = 3; width < 13; width++) {
                if (toCreate[height][width]
                        && (grid.getBoard()[height][width] == 0 || grid.getBoard()[height][width] == EXTEND_BLOCK)) {  // 확장 아이템 블록 발견
                    grid.getBoard()[height][width] = EXTEND_BLOCK_EVENT;
                    toCreate[height][width] = false;
                }
            }
        }

        lineCheck();
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public int getStopCount() {
        return stopCount;
    }

    public int getLimitCount() {
        return limitCount;
    }

    public boolean isCanPlaceBlock() {
        return canPlaceBlock;
    }

    public int getPlacedBlockCount() {
        return placedBlockCount;
    }

    public boolean isCanMoveSide() {
        return canMoveSide;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
