package logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreController {
    // 파일 경로를 2개로 나눕니다.
    private final String normalFilePath = "normalRanking.txt";
    private final String itemFilePath = "itemRanking.txt";
    private List<Score> normalScores;
    private List<Score> itemScores;

    public ScoreController() {
        normalScores = new ArrayList<>();
        itemScores = new ArrayList<>();
        loadScores(normalFilePath, normalScores); // normal 점수 로드
        loadScores(itemFilePath, itemScores); // item 점수 로드
    }

    // 파일로부터 점수를 불러오는 메소드, filePath와 scores 리스트를 매개변수로 받습니다.
    private void loadScores(String filePath, List<Score> scores) {
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    scores.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
            }
            Collections.sort(scores); // 점수를 내림차순으로 정렬합니다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 점수를 파일에 저장, filePath와 scores 리스트를 매개변수로 받습니다.
    private void saveScores(String filePath, List<Score> scores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Score score : scores) {
                writer.write(score.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 새로운 점수를 추가, 점수 유형에 따라 다른 리스트에 추가하도록 메소드를 수정합니다.
    public void addScore(String playerName, int score, boolean isItem) {
        List<Score> targetScores = isItem ? itemScores : normalScores;
        targetScores.add(new Score(playerName, score));
        Collections.sort(targetScores);

        while (targetScores.size() > 10) {
            targetScores.remove(targetScores.size() - 1);
        }

        saveScores(isItem ? itemFilePath : normalFilePath, targetScores);
    }

    // 모든 점수를 초기화하는 메소드, 점수 유형에 따라 다른 리스트를 초기화하도록 변경합니다.
    public void resetScores(boolean isItem) {
        List<Score> targetScores = isItem ? itemScores : normalScores;
        targetScores.clear();
        saveScores(isItem ? itemFilePath : normalFilePath, targetScores);
    }

    // 점수 목록을 가져오기, 점수 유형에 따라 다른 리스트를 반환하도록 변경합니다.
    public List<Score> getScores(boolean isItem) {
        return isItem ? itemScores : normalScores;
    }

    // 점수가 Top10 안에 드는지 검사, 점수 유형에 따라 다른 리스트에서 검사합니다.
    public boolean isScoreInTop10(int score, boolean isItem) {
        List<Score> targetScores = isItem ? itemScores : normalScores;

        if (targetScores.size() < 10) {
            return true;
        }

        return score > targetScores.get(9).getScore();
    }
}
