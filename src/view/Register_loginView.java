package view;

import api.ClientSocket;
import controller.Client_Login_registerController;
import exception.CustomException;
import model.RegistrationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Register_loginView {

    private final ClientSocket clientSocket;
    private JTextField usernameField;
    private JFrame frame;
    private JPasswordField passwordField;
    private final Client_Login_registerController loginRegisterController;

    public Register_loginView(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
        loginRegisterController = new Client_Login_registerController(clientSocket);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField = new JPasswordField();

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        panel.add(inputPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.setBackground(Color.CYAN);
        registerButton.setBackground(Color.CYAN);

        loginButton.setBorder(BorderFactory.createRaisedBevelBorder());
        registerButton.setBorder(BorderFactory.createRaisedBevelBorder());


        loginButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setPreferredSize(new Dimension(120, 40));


        loginButton.setMargin(new Insets(10, 10, 10, 10));
        registerButton.setMargin(new Insets(10, 10, 10, 10));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        panel.add(buttonPanel);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void login() {
        // Set Username & Password
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        // Check From Username & Password
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter the username and password",
                    "Missing Data", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                // Do login Operation
                // Server Will Get user From DB and return userInfo
                RegistrationModel userInfo = loginRegisterController.login(username, password);
                //Check From userInfo
                if (userInfo == null) {
                    JOptionPane.showMessageDialog(frame, "user not found, Or username / password is incorrect",
                            "Login Fail", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println(userInfo.id);
                    System.out.println(userInfo.role);
                    // Navigate to Student Projects View
                    if (Objects.equals(userInfo.role, "Student")) {
                        ProjectsView pro = new ProjectsView(clientSocket, userInfo);
                    } else {
                        // Navigate to PhD Marks View
                        MarksView mar = new MarksView(clientSocket, userInfo);
                    }
                    frame.dispose();
                }
            } catch (CustomException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void signUp() {
        // Set Username & Password
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        // Check From Username & Password
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter the username and password");
        } else {
            try {
                // Do SignUp Operation
                // Create User record in DB
                int userId = loginRegisterController.register(username, password);
                // Check From Response
                if (userId == 0) {
                    JOptionPane.showMessageDialog(frame, "The user already exists",
                            "SignUp Fail" , JOptionPane.WARNING_MESSAGE);
                } else {
                    // Navigate to Registration View to Continue User Info
                    RegistrationForm f = new RegistrationForm(clientSocket, userId, username);
                    frame.dispose();
                }
            } catch (CustomException ex) {
                ex.printStackTrace();
            }

        }
    }
}

















