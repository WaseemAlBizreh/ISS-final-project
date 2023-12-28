package view;
import api.ClientSocket;
import api.ClientSocket;
import api.Operation;
import exception.CustomException;
import model.LoginRegisterModel;
import model.Message;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import controller.*;
import model.RegistrationModel;

public class Register_loginView {

    private final ClientSocket clientSocket;

    public Register_loginView(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
        createAndShowGUI();


    }

        private  void createAndShowGUI() {
            JFrame frame = new JFrame("Login");
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
            JTextField usernameField = new JTextField();
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JPasswordField passwordField = new JPasswordField();

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

                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());
                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter the username and password");
                    }
                    else {
                        Client_Login_registerController gg = new Client_Login_registerController(clientSocket);
                        try {
                            RegistrationModel ss = gg.login(username, password);
                            if (ss == null) {
                                JOptionPane.showMessageDialog(frame, "The username or password is incorrect");
                            } else {
                                System.out.println(ss.id);
                                System.out.println(ss.role);

                                if (Objects.equals(ss.role, "Student")) {
                                    ProjectsView pro = new ProjectsView(clientSocket, ss);
                                } else {
                                    MarksView mar = new MarksView(clientSocket, ss);
                                }
                                frame.dispose();
                            }


                        } catch (CustomException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter the username and password");
                    }
                    else {
                        Client_Login_registerController gg = new Client_Login_registerController(clientSocket);
                        try {

                            //    RegistrationModel ss=gg.login(username, password);

                            int r = gg.register(username, password);
                            System.out.println(r);
                            if (r == 0) {
                                JOptionPane.showMessageDialog(frame, "The user already exists");
                            } else {
                                RegistrationForm f = new RegistrationForm(clientSocket, r);
                                frame.dispose();
                            }
                        } catch (CustomException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        }
    }

















