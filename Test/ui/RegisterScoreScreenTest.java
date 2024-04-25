package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class RegisterScoreScreenTest {

    private RegisterScoreScreen registerScoreScreen;
    private JTextField nameField;
    private JButton submitButton;

    @BeforeEach
    void setUp() {
        // Initialize RegisterScoreScreen on the Event Dispatch Thread to ensure all UI components are properly instantiated
        try {
            SwingUtilities.invokeAndWait(() -> registerScoreScreen = new RegisterScoreScreen(100, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
        nameField = (JTextField) findComponent(registerScoreScreen, JTextField.class);
        submitButton = (JButton) findComponent(registerScoreScreen, JButton.class);
    }

//    @Test
//    void testTextFieldBehavior() {
//        // Simulate focus gained
//        SwingUtilities.invokeLater(() -> {
//            // nameField.requestFocus();
//            KeyEvent keyEvent = new KeyEvent(nameField, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, KeyEvent.CHAR_UNDEFINED);
//            nameField.dispatchEvent(keyEvent);
//            assertEquals("", nameField.getText()); // Expect the default text to be cleared
//
//            // Simulate focus lost without input
//            nameField.setText("");
//            nameField.transferFocus();
//            assertEquals("Please register playerName", nameField.getText()); // Expect the default text to reappear
//        });
//    }

    @Test
    void actionPerformed_Submit() {
        // Given
        RegisterScoreScreen registerScoreScreen = new RegisterScoreScreen(100, false);
        registerScoreScreen.nameField = new JTextField("Test Player");

        // When
        registerScoreScreen.actionPerformed(createActionEvent("submit"));

        // Then
        // Add assertions to verify the expected behavior after submitting the score
        // For example, you can check if the RegisterScoreScreen is no longer visible
        assertFalse(registerScoreScreen.isVisible());
    }

    // Helper method to create ActionEvent for testing
    private ActionEvent createActionEvent(String command) {
        return new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, command);
    }

    private Component findComponent(Container container, Class<?> componentClass) {
        for (Component comp : container.getComponents()) {
            if (componentClass.isInstance(comp)) {
                return comp;
            }
            if (comp instanceof Container) {
                Component found = findComponent((Container) comp, componentClass);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
