package logic;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingProperties {
    public static final String DEFAULT_COLOR_MODE = "default";
    public static final String COLOR_BLIND_MODE = "colorBlindMode";
    public static final String DIFFICULTY = "difficulty";
    public static final String configPath = "config.properties";

    public static final ArrayList<String> KEYS = new ArrayList<>(
            Arrays.asList("↑", "←", "→", "↓", "␣")
            // → ↑ ← → ␣ -space bar ⇧ - shift ⇪ - kor/eng
    );


}
