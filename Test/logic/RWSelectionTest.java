package logic;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RWSelectionTest {

    @Test
    void select() {
    }

    @Test
    public void testBlockDistribution_easy() {
        double[] expectedDistribution = {12, 10, 10, 10, 10, 10, 10};
        int difficulty = 0;
        RWSelection selection = new RWSelection(difficulty);

        int repetitions = 100000;
        HashMap<Integer, Integer> counts = new HashMap<>();

        // 100000번 선택 반복
        for (int i = 0; i < repetitions; i++) {
            int selectedIndex = selection.select();
            counts.put(selectedIndex, counts.getOrDefault(selectedIndex, 0) + 1);
        }

        // 각 인덱스가 ±5% 오차범위 내에서 선택되었는지 확인
        for (int i = 0; i < expectedDistribution.length; i++) {
            double expected = expectedDistribution[i] * (repetitions / 72); // 총합에 따라 배율 적용
            double lowerBound = expected * 0.95; // 오차범위 -5%
            double upperBound = expected * 1.05; // 오차범위 +5%

            // 실제 선택 횟수
            int actualCount = counts.getOrDefault(i, 0);

            boolean isWithinRange = actualCount >= lowerBound && actualCount <= upperBound;

            assertTrue(isWithinRange,
                    "Block index " + i + " is outside acceptable range. Actual: " + actualCount + ", Expected: " + lowerBound + " - " + upperBound);
        }
    }
    @Test
    public void testBlockDistribution_normal() {
        double[] expectedDistribution = {10, 10, 10, 10, 10, 10, 10};
        int difficulty = 1;
        RWSelection selection = new RWSelection(difficulty);

        int repetitions = 100000;
        HashMap<Integer, Integer> counts = new HashMap<>();

        // 100000번 선택 반복
        for (int i = 0; i < repetitions; i++) {
            int selectedIndex = selection.select();
            counts.put(selectedIndex, counts.getOrDefault(selectedIndex, 0) + 1);
        }

        // 각 인덱스가 ±5% 오차범위 내에서 선택되었는지 확인
        for (int i = 0; i < expectedDistribution.length; i++) {
            double expected = expectedDistribution[i] * (repetitions / 70); // 총합에 따라 배율 적용
            double lowerBound = expected * 0.95; // 오차범위 -5%
            double upperBound = expected * 1.05; // 오차범위 +5%

            // 실제 선택 횟수
            int actualCount = counts.getOrDefault(i, 0);

            boolean isWithinRange = actualCount >= lowerBound && actualCount <= upperBound;

            assertTrue(isWithinRange,
                    "Block index " + i + " is outside acceptable range. Actual: " + actualCount + ", Expected: " + lowerBound + " - " + upperBound);
        }
    }
    @Test
    public void testBlockDistribution_hard() {
        double[] expectedDistribution = {8, 10, 10, 10, 10, 10, 10};
        int difficulty = 2;
        RWSelection selection = new RWSelection(difficulty);

        int repetitions = 100000;
        HashMap<Integer, Integer> counts = new HashMap<>();

        // 100000번 선택 반복
        for (int i = 0; i < repetitions; i++) {
            int selectedIndex = selection.select();
            counts.put(selectedIndex, counts.getOrDefault(selectedIndex, 0) + 1);
        }

        // 각 인덱스가 ±5% 오차범위 내에서 선택되었는지 확인
        for (int i = 0; i < expectedDistribution.length; i++) {
            double expected = expectedDistribution[i] * (repetitions / 68); // 총합에 따라 배율 적용
            double lowerBound = expected * 0.95; // 오차범위 -5%
            double upperBound = expected * 1.05; // 오차범위 +5%

            // 실제 선택 횟수
            int actualCount = counts.getOrDefault(i, 0);

            boolean isWithinRange = actualCount >= lowerBound && actualCount <= upperBound;

            assertTrue(isWithinRange,
                    "Block index " + i + " is outside acceptable range. Actual: " + actualCount + ", Expected: " + lowerBound + " - " + upperBound);
        }
    }
    @Test
    public void testBlockDistribution_item() {
        double[] expectedDistribution = {10, 10, 10, 10, 10};
        int difficulty = 3;
        RWSelection selection = new RWSelection(difficulty);

        int repetitions = 100000;
        HashMap<Integer, Integer> counts = new HashMap<>();

        // 100000번 선택 반복
        for (int i = 0; i < repetitions; i++) {
            int selectedIndex = selection.select();
            counts.put(selectedIndex, counts.getOrDefault(selectedIndex, 0) + 1);
        }

        // 각 인덱스가 ±5% 오차범위 내에서 선택되었는지 확인
        for (int i = 0; i < expectedDistribution.length; i++) {
            double expected = expectedDistribution[i] * (repetitions / 50); // 총합에 따라 배율 적용
            double lowerBound = expected * 0.95; // 오차범위 -5%
            double upperBound = expected * 1.05; // 오차범위 +5%

            // 실제 선택 횟수
            int actualCount = counts.getOrDefault(i, 0);

            boolean isWithinRange = actualCount >= lowerBound && actualCount <= upperBound;

            assertTrue(isWithinRange,
                    "Block index " + i + " is outside acceptable range. Actual: " + actualCount + ", Expected: " + lowerBound + " - " + upperBound);
        }
    }
    @Test
    public void testBlockDistribution_invalid() {
        int difficulty = 4;
        assertThrows(IllegalArgumentException.class, () -> new RWSelection(difficulty));
    }
}
