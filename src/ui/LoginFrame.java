package ui;

import service.UserService;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class LoginFrame extends JFrame {

    private UserService userService;
    private JLabel statusLabel;
    private JTextField userField;
    private JPasswordField passField;

    // Colors
    private final Color PRIMARY_COLOR = new Color(51, 102, 255); // Nice Blue
    private final Color BG_COLOR = new Color(245, 247, 250); // Light Grey
    private final Color CARD_BG = Color.WHITE;
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    public LoginFrame() {
        this.userService = new UserService();

        setTitle("Login System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // --- Top Bar (Utilities) ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        topBar.setOpaque(false);

        statusLabel = new JLabel("Users loaded: No");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));

        JButton importCsvBtn = createStyledButton("Import from CSV (Default)", false);
        importCsvBtn.addActionListener(this::onImportDefault);

        JButton importFileBtn = createStyledButton("Import Users...", false);
        importFileBtn.addActionListener(this::onImportFile);

        topBar.add(statusLabel);
        topBar.add(importCsvBtn);
        topBar.add(importFileBtn);

        add(topBar, BorderLayout.NORTH);

        // --- Center Login Card ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBackground(CARD_BG);
        loginCard.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Shadow/Border effect for card (Simple line border for now)
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(40, 60, 40, 60)
        ));

        // Header
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Please enter your details");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitleLabel.setForeground(new Color(120, 120, 120));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        userField = createStyledTextField();
        passField = createStyledPasswordField();

        JButton loginBtn = createStyledButton("Sign In", true);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(this::onLogin);

        // Layout Components in Card
        loginCard.add(titleLabel);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(subTitleLabel);
        loginCard.add(Box.createVerticalStrut(40));

        loginCard.add(createLabel("Username"));
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(userField);
        loginCard.add(Box.createVerticalStrut(20));

        loginCard.add(createLabel("Password"));
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(passField);
        loginCard.add(Box.createVerticalStrut(30));

        loginCard.add(loginBtn);

        centerPanel.add(loginCard);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void onImportDefault(ActionEvent e) {
        String defaultPath = "data/users.csv";
        boolean success = userService.loadUsers(defaultPath);
        updateStatus(success);
    }

    private void onImportFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            boolean success = userService.loadUsers(selectedFile.getAbsolutePath());
            updateStatus(success);
        }
    }

    private void updateStatus(boolean success) {
        if (success) {
            statusLabel.setText("Users loaded: Yes (" + userService.getUserCount() + ")");
            statusLabel.setForeground(new Color(40, 167, 69)); // Green
            JOptionPane.showMessageDialog(this, "Users loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            statusLabel.setText("Users loaded: Error");
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this, "Failed to load users.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLogin(ActionEvent e) {
        String user = userField.getText();
        String pass = new String(passField.getPassword());

        if (userService.getUserCount() == 0) {
             JOptionPane.showMessageDialog(this, "No users loaded. Please import users first.", "Warning", JOptionPane.WARNING_MESSAGE);
             return;
        }

        User authenticatedUser = userService.authenticate(user, pass);
        if (authenticatedUser != null) {
            JOptionPane.showMessageDialog(this, "Login Successful!\nWelcome, " + authenticatedUser.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- UI Helpers ---

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Important for BoxLayout
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JButton createStyledButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (primary) {
            btn.setBackground(PRIMARY_COLOR);
            btn.setForeground(Color.WHITE);
            btn.setBorder(new EmptyBorder(10, 30, 10, 30));
            // Basic "flat" look for Swing
            btn.setOpaque(true);
            btn.setBorderPainted(false);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(TEXT_COLOR);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(8, 15, 8, 15)
            ));
        }
        return btn;
    }
}
