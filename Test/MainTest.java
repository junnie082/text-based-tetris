import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void main() {
        // 1. Run the main method
        Main.main(new String[]{});

        // 2. Check that the main method runs without throwing an exception
        assertTrue(true, "Main method should run without throwing an exception.");
    }
}
