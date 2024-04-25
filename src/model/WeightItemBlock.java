package model;

import java.awt.*;
import logic.Block;

public class WeightItemBlock extends Block {
    // 움직여도 되는지
    public WeightItemBlock() {
        super(new int[][] {
                {0, 0, 0, 0},
                {0, 9, 9, 0},
                {9, 9, 9, 9},
                {9, 9, 9, 9},
        }, new Color[]{Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY}); // 색상은 예시, 실제 게임에 맞게 조정
    }

    @Override
    public void rotate() {
        // 이 블록은 회전할 수 없습니다.
    }

    @Override
    public void rotateBack() {
        // 이 블록은 회전할 수 없습니다.
    }
}
