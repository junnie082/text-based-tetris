package model;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BombItemBlockTest {

    @Test
    void testBlockShapeAndColor() {
        // Arrange
        BombItemBlock bombBlock = new BombItemBlock();
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {0, 0, 12, 0},
                {0, 11, 11, 0},
                {0, 11, 11, 0},
        };
        Color[] expectedColors = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};

        // Act
        int[][] actualShape = bombBlock.getShape();
        Color[] actualColors = bombBlock.getColors();

        // Assert
        assertArrayEquals(expectedShape, actualShape);
        assertArrayEquals(expectedColors, actualColors);
    }

    @Test
    void testRotate_doesNothing() {
        // Arrange
        BombItemBlock bombBlock = new BombItemBlock();
        int[][] initialShape = bombBlock.getShape();

        // Act
        bombBlock.rotate();
        int[][] shapeAfterRotate = bombBlock.getShape();

        // Assert
        assertArrayEquals(initialShape, shapeAfterRotate);
    }

    @Test
    void testRotateBack_doesNothing() {
        // Arrange
        BombItemBlock bombBlock = new BombItemBlock();
        int[][] initialShape = bombBlock.getShape();

        // Act
        bombBlock.rotateBack();
        int[][] shapeAfterRotateBack = bombBlock.getShape();

        // Assert
        assertArrayEquals(initialShape, shapeAfterRotateBack);
    }
}
