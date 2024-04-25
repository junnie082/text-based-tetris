package logic;

import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static logic.SettingProperties.COLOR_BLIND_MODE;
import static logic.SettingProperties.DEFAULT_COLOR_MODE;

public abstract class Block {
    protected int[][] shape;
    protected Color[] colors;
    private final SettingController settingController;
    private final RWSelection rwSelection;
    private static int erasedLineCountForItem = 0;

    public Block(int[][] shape, Color[] colors) {
        settingController = new SettingController();
        rwSelection = new RWSelection(settingController.getDifficulty());
        this.shape = shape;
        this.colors = colors;
    }

    public int getShape(int x, int y) {
        return shape[y][x];
    }

    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        String mode = settingController.getColorMode(COLOR_BLIND_MODE, DEFAULT_COLOR_MODE);

        switch (mode) {
            case "protanopia":
                return colors[1]; // 적생맹
            case "deuteranopia":
                return colors[2]; // 녹생맹
            case "tritanopia":
                return colors[3]; // 청색맹
            default:
                return colors[0]; // 기본 색상
        }
    }

    public Color[] getColors() {
        return colors;
    }

    public int[][] shapeCopy() {
        int[][] copy = new int[shape.length][shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            System.arraycopy(shape[i], 0, copy[i], 0, shape[i].length);
        }
        return copy;
    }

    public int height() {
        return shape.length;
    }

    public int width() {
        return shape[0].length;
    }

    public void rotate() {
        rotateArray(true);
    }

    public void rotateBack() {
        rotateArray(false);
    }

    // Rotate the block clockwise or counterclockwise
    private void rotateArray(boolean clockwise) {
        int n = shape.length;
        for (int i = 0; i < n / 2; i++) {
            for (int j = i; j < n - i - 1; j++) {
                int temp = shape[i][j];
                if (clockwise) {
                    shape[i][j] = shape[n - j - 1][i];
                    shape[n - j - 1][i] = shape[n - i - 1][n - j - 1];
                    shape[n - i - 1][n - j - 1] = shape[j][n - i - 1];
                    shape[j][n - i - 1] = temp;
                } else {
                    shape[i][j] = shape[j][n - i - 1];
                    shape[j][n - i - 1] = shape[n - i - 1][n - j - 1];
                    shape[n - i - 1][n - j - 1] = shape[n - j - 1][i];
                    shape[n - j - 1][i] = temp;
                }
            }
        }
    }

    public static Block getBlock(BlockType blockType) {
        return switch (blockType) {
            case IBlock -> new IBlock();
            case JBlock -> new JBlock();
            case LBlock -> new LBlock();
            case OBlock -> new OBlock();
            case SBlock -> new SBlock();
            case TBlock -> new TBlock();
            case ZBlock -> new ZBlock();
            case NullBlock -> new NullBlock();
            case WeightItemBlock -> new WeightItemBlock();
            case BombItemBlock -> new BombItemBlock();
            case ExtensionItemBlock -> new ExtensionItemBlock();
            case ItemBlock -> new ItemBlock();
        };
    }

    public static void setErasedLineCountForItem(int count) {
        erasedLineCountForItem = count;
    }

    public static int getErasedLineCountForItem() {
        return erasedLineCountForItem;
    }

    //select for the block
    public Block selectBlock(boolean isItem, int erasedLineCount) {
        // 10줄이 삭제될 때마다 아이템 블록 생성 로직
        if (isItem && erasedLineCount % 10 == 0 && getErasedLineCountForItem() < erasedLineCount) {
            setErasedLineCountForItem(erasedLineCount);
            return selectItemBlock();
        } else {
            // 일반 블록 선택 로직 (10줄이 삭제되지 않았을 때)
            int blockType = rwSelection.select();
            // 기본 블록 선택 로직
            return switch (blockType) {
                case 0 -> new IBlock();
                case 1 -> new JBlock();
                case 2 -> new LBlock();
                case 3 -> new OBlock();
                case 4 -> new SBlock();
                case 5 -> new TBlock();
                case 6 -> new ZBlock();
                default -> throw new IllegalArgumentException("Invalid block type.");
            };
        }
    }

    public Block selectItemBlock() {
        RWSelection rwSelection = new RWSelection(3);
        return switch (rwSelection.select()) {
            case 0 -> new ItemBlock().waterBlock();
            case 1 -> new ItemBlock().lineBlock();
            case 2 -> new BombItemBlock();
            case 3 -> new ExtensionItemBlock();
            default -> new WeightItemBlock();
        };
    }
}
