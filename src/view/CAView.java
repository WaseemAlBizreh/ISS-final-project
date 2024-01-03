package view;

import api.ClientSocket;
import api.Operation;
import controller.Client_Login_registerController;
import exception.CustomException;
import model.Message;
import model.RegistrationModel;
import security.SessionKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Objects;

public class CAView {
    private JFrame frame;
    private JTextField serverIPField;
    private JTextField serverPortField;
    private JTextField usernameField;
    private final ObjectOutputStream sender;
    private final ObjectInputStream receiver;
    private JPasswordField passwordField;
    private final Client_Login_registerController loginRegisterController;
    private final ClientSocket clientSocket;
    private final KeyPair keys;
    private final SessionKey sessionKey;

    public CAView(ClientSocket clientSocket, KeyPair keys,SessionKey sessionKey) {
        loginRegisterController = new Client_Login_registerController(clientSocket);
        this.clientSocket = clientSocket;
        this.keys = keys;
        this.sessionKey =sessionKey;

        // Establish Sender
            sender = clientSocket.sender;
        // Establish Receiver
        receiver = clientSocket.receiver;

        createAndShowGUI();
    }
    private void createAndShowGUI() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 300);
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

        loginButton.setBackground(Color.CYAN);

        loginButton.setBorder(BorderFactory.createRaisedBevelBorder());


        loginButton.setPreferredSize(new Dimension(120, 40));


        loginButton.setMargin(new Insets(10, 10, 10, 10));

        buttonPanel.add(loginButton);

        panel.add(buttonPanel);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
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
                Message usernameMessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey.getSessionKey());
                System.out.println(usernameMessage.getMessage());
                //send username
                Message UserName=new Message(username, Operation.None);
                sender.writeObject(SessionKey.encrypt(UserName,sessionKey.getSessionKey()));

                String passwordMessage=(String) receiver.readObject();
                System.out.println(SessionKey.decrypt(passwordMessage,sessionKey.getSessionKey()).getMessage());
                //send password
                Message Password=new Message(password, Operation.None);
                sender.writeObject(SessionKey.encrypt(Password,sessionKey.getSessionKey()));
                Message equation=SessionKey.decrypt((String)receiver.readObject(),sessionKey.getSessionKey());
                EquationUI equationUI=new EquationUI(equation.getMessage(),clientSocket,keys,sessionKey);


                    frame.dispose();
                }
            catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
