package logic;

import java.util.Objects;

public class Score implements Comparable<Score> {
    private final String playerName;
    private final int score;
    private final String difficulty;

    SettingController settingController = new SettingController();

    public Score(String playerName, int score, String difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.difficulty = difficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public String getDifficulty() {
        return difficulty;
    }

    // 점수에 따라 Score 객체를 비교하는 메소드
    @Override
    public int compareTo(Score other) {
        return Integer.compare(other.score, this.score); // 내림차순 정렬
    }

    // 파일에 저장하거나 읽어올 때 사용할 문자열 형식
    @Override
    public String toString() {

        return playerName + ":" + difficulty + ":" + score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return this.score == score.score &&
                Objects.equals(playerName, score.playerName) &&
                Objects.equals(difficulty, score.difficulty); // 난이도도 비교에 포함
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, score, difficulty); // 난이도도 해시 코드 계산에 포함
    }
}