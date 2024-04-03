package logic;

import model.*;

import java.awt.Color;

import static logic.SettingProperties.COLOR_BLIND_MODE;
import static logic.SettingProperties.DEFAULT_COLOR_MODE;

public abstract class Block {
    protected int[][] shape;
    protected Color[] colors;
    private final SettingController settingController;

    public Block(int[][] shape, Color[] colors) {
        settingController = new SettingController();
        this.shape = shape;
        this.colors = colors;
    }

    public int getShape(int x, int y) {
        return shape[y][x];
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
        };
    }
}
