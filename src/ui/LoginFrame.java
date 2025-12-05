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

        JButton importCsvBtn = createStyledButton("Import Users", false);
        importCsvBtn.addActionListener(this::onImportDefault);

        topBar.add(statusLabel);
        topBar.add(importCsvBtn);

        add(topBar, BorderLayout.NORTH);

        // --- Center Login Card ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBackground(CARD_BG);
        loginCard.setBorder(new EmptyBorder(40, 50, 40, 50)); // Padding

        // Fixed width for the card so fields look "full size" relative to this card
        loginCard.setPreferredSize(new Dimension(450, 600));

        // Shadow/Border effect
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(40, 50, 40, 50)
        ));

        // --- Image (Top, Short) ---
        ImageIcon originalIcon = new ImageIcon("static/images/user_placeholder.png");
        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Short/Small
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Full width button
        loginBtn.addActionListener(this::onLogin);

        JButton registerBtn = createStyledButton("Register", false);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Full width button
        registerBtn.addActionListener(this::onRegister);

        // Add components
        loginCard.add(Box.createVerticalGlue());
        loginCard.add(imageLabel);
        loginCard.add(Box.createVerticalStrut(20));

        loginCard.add(titleLabel);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(subTitleLabel);
        loginCard.add(Box.createVerticalStrut(30));

        loginCard.add(createLabel("Username"));
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(userField);
        loginCard.add(Box.createVerticalStrut(20));

        loginCard.add(createLabel("Password"));
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(passField);
        loginCard.add(Box.createVerticalStrut(30));

        loginCard.add(loginBtn);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(registerBtn);
        loginCard.add(Box.createVerticalGlue());

        centerPanel.add(loginCard);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void onImportDefault(ActionEvent e) {
        String defaultPath = "data/users.csv";
        boolean success = userService.loadUsers(defaultPath);
        if (success) {
            statusLabel.setText("Users loaded: Yes (" + userService.getUserCount() + ")");
            statusLabel.setForeground(new Color(40, 167, 69)); // Green
            JOptionPane.showMessageDialog(this, "Import from '" + defaultPath + "' " + userService.getUserCount() + " users", "Import Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            statusLabel.setText("Users loaded: Error");
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this, "Failed to load users from " + defaultPath, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRegister(ActionEvent e) {
        JTextField regUserField = new JTextField();
        JPasswordField regPassField = new JPasswordField();
        JTextField regNameField = new JTextField();

        Object[] message = {
            "Username:", regUserField,
            "Password:", regPassField,
            "Name:", regNameField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register New User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = regUserField.getText();
            String pass = new String(regPassField.getPassword());
            String name = regNameField.getText();

            if (user.isEmpty() || pass.isEmpty() || name.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            // Hardcoded path as per requirements
            boolean success = userService.registerUser("data/users.csv", user, pass, name);
            if (success) {
                JOptionPane.showMessageDialog(this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("Users loaded: Yes (" + userService.getUserCount() + ")");
                statusLabel.setForeground(new Color(40, 167, 69));
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. User might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            // JOptionPane.showMessageDialog(this, "Login Successful!\nWelcome, " + authenticatedUser.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            new MainFrame().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- UI Helpers ---

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_COLOR);
        // Make label take full width so text stays left even if component is centered
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        // Remove preferred size width limit to allow full width
        field.setPreferredSize(new Dimension(0, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        // Remove preferred size width limit to allow full width
        field.setPreferredSize(new Dimension(0, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
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
