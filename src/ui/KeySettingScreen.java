package src.ui;

import logic.SettingController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static component.Button.createBtn;

public class KeySettingScreen extends JFrame {
    private JLabel[] labels = new JLabel[5];
    public JTextField[] textFields = new JTextField[5];
    private JButton btnInitialize = createBtn("Initialize", "initialize", this::actionPerformed);
    private JButton btnBack = createBtn("Back", "back", this::actionPerformed);

    private int focusedIndex = 0;

    private SettingController settingController = new SettingController();

    public KeySettingScreen() {
        setTitle("Key Setting");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize labels and text fields
        for (int i = 0; i < textFields.length; i++) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            labels[i] = new JLabel();
            if (i == 0) {
                labels[i].setText("Change Shape");
                textFields[i] = new JTextField("Press Enter to Set Key");
            } else if (i == 1) {
                labels[i].setText("Left Key");
                textFields[i] = new JTextField("Press Enter to Set Key");
            } else if (i == 2) {
                labels[i].setText("Right Key");
                textFields[i] = new JTextField("Press Enter to Set Key");
            } else if (i == 3) {
                labels[i].setText("Go down faster");
                textFields[i] = new JTextField("Press Enter to Set Key");
            } else if (i == 4) {
                labels[i].setText("Go down at once");
                textFields[i] = new JTextField("Press Enter to Set Key");
            }
            textFields[i].setEditable(false);
            textFields[i].setBackground(Color.WHITE);
            textFields[i].setHorizontalAlignment(JTextField.CENTER);

            panel.add(labels[i]);
            panel.add(textFields[i]);

            add(panel);
        }

        btnInitialize.addKeyListener(new MyKeyListener());
        btnBack.addKeyListener(new MyKeyListener());

        add(Box.createVerticalStrut(20));
        add(btnInitialize);
        add(btnBack);
        btnInitialize.setAlignmentX(Component.CENTER_ALIGNMENT);

        updateFocus();

        btnInitialize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JTextField textField : textFields) {
                    textField.setText("Press Enter to Set Key");
                }
            }
        });

        // 키 바인딩 설정
        setFocusable(true);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "upAction");
        getRootPane().getActionMap().put("upAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                focusedIndex = (focusedIndex - 1 + textFields.length) % textFields.length;
                updateFocus();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "downAction");
        getRootPane().getActionMap().put("downAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                focusedIndex = (focusedIndex + 1) % textFields.length;
                updateFocus();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
        getRootPane().getActionMap().put("enterAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterInputMode();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER) {
                applySetting();
            }
        }
    }

    private void applySetting() {
        if (btnInitialize.isFocusOwner()) {

        } else if (btnBack.isFocusOwner()) {
            setVisible(false);
            new ui.MainMenuScreen();
        }
    }


    private void updateFocus() {
        for (int i = 0; i < textFields.length; i++) {
            if (i == focusedIndex) {
                textFields[i].setBackground(Color.YELLOW);
            } else {
                textFields[i].setBackground(Color.WHITE);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("initialize")) {

        } else if (command.equals("back")) {
            setVisible(false);
            settingController.setKeys(textFields);
            new ui.SettingScreen();
        }
    }

    private void enterInputMode() {

        JDialog inputDialog = new JDialog(this, "Input Key", true);
        inputDialog.setLayout(new FlowLayout());
        inputDialog.setSize(300, 100);

        JLabel instructionLabel = new JLabel("Press any key...");
        inputDialog.add(instructionLabel);

        inputDialog.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                String keyString = KeyEvent.getKeyText(keyCode);
                //→ ↑ ← → ␣ -space bar ⇧ - shift ⇪ - kor/eng

                // Disallowing certain keys
                if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_ENTER ||
                        keyCode == KeyEvent.VK_WINDOWS || keyCode == KeyEvent.VK_CONTROL ||
                        keyCode == KeyEvent.VK_META || keyCode == KeyEvent.VK_ALT) {
                    JOptionPane.showMessageDialog(KeySettingScreen.this, "This key is not allowed to be assigned.", "Invalid Key", JOptionPane.WARNING_MESSAGE);
                    inputDialog.dispose();
                    return;
                }

                boolean keyAssigned = false;
                for (JTextField textField : textFields) {
                    if (textField.getText().equals(keyString)) {
                        keyAssigned = true;
                        break;
                    }
                }
                if (!keyAssigned) {
                    textFields[focusedIndex].setText(keyString);
                } else {
                    JOptionPane.showMessageDialog(KeySettingScreen.this, "This key is already assigned to another section.", "Key Already Assigned", JOptionPane.WARNING_MESSAGE);
                }
                inputDialog.dispose();
            }
        });

        inputDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KeySettingScreen());
    }
}
