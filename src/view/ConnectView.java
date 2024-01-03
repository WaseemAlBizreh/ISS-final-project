package view;

import api.ClientSocket;
import security.SessionKey;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.KeyPair;

public class ConnectView {
    private JFrame frame;
    private JTextField serverIPField;
    private JTextField serverPortField;

    private final ClientSocket clientSocket;
    private final KeyPair keys;

    public ConnectView(ClientSocket clientSocket, KeyPair keys) {
        this.clientSocket = clientSocket;
        this.keys = keys;
        buildScreen();
    }

    private void connectToServer() {

        String serverIP = serverIPField.getText();
        String serverPortText = serverPortField.getText();
        // Check for missing data
        if (serverIP.isEmpty() || serverPortText.isEmpty()) {
            // Show a notification if there is missing data
            JOptionPane.showMessageDialog(frame, "Please enter both Server IP and Server Port.",
                    "Missing Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int serverPort = Integer.parseInt(serverPortText);

            // Call the connectToServer method in the client controller

            boolean connect = clientSocket.connectToServer(serverIP, serverPort);

            //Close Dialog
            if (connect) {
                JOptionPane.showMessageDialog(frame, "You Connect with Server Successfully.",
                        "Connect Successfully", JOptionPane.INFORMATION_MESSAGE);
                //   RegistrationForm m = new RegistrationForm(clientSocket);
                Register_loginView loginSignUp = new Register_loginView(clientSocket, keys);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Enter valid Server Port.",
                        "Invalid Port", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | IOException e) {
            // Handle the case where the port is not a valid integer
            JOptionPane.showMessageDialog(frame, "Please enter a valid Server Port.",
                    "Invalid Port", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void connectToCA() {
        String serverIP = serverIPField.getText();

        String serverPortText = serverPortField.getText();
        // Check for missing data
        if (serverIP.isEmpty() || serverPortText.isEmpty()) {
            // Show a notification if there is missing data
            JOptionPane.showMessageDialog(frame, "Please enter both Server IP and Server Port.",
                    "Missing Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int serverPort = Integer.parseInt(serverPortText);

            // Call the connectToServer method in the client controller

            SessionKey connect = clientSocket.connectToCA(serverIP, serverPort);

            //Close Dialog
            if (connect!=null) {

                JOptionPane.showMessageDialog(frame, "You Connect with CA Successfully.",
                        "Connect Successfully", JOptionPane.INFORMATION_MESSAGE);
                //   RegistrationForm m = new RegistrationForm(clientSocket);
                //Register_loginView loginSignUp = new Register_loginView(clientSocket, keys);
                CAView caView=new CAView(clientSocket,keys,connect);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Enter valid Server Port.",
                        "Invalid Port", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | IOException e) {
            // Handle the case where the port is not a valid integer
            JOptionPane.showMessageDialog(frame, "Please enter a valid Server Port.",
                    "Invalid Port", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buildScreen() {
        //   Register_loginView f = new Register_loginView(clientSocket);
        frame = new JFrame("Connect View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLayout(null);

        JLabel serverIPLabel = new JLabel("Server IP:");
        serverIPLabel.setBounds(30, 20, 80, 30);

        serverIPField = new JTextField();
        serverIPField.setBounds(130, 20, 170, 30);

        JLabel serverPortLabel = new JLabel("Server Port:");
        serverPortLabel.setBounds(30, 60, 80, 30);

        serverPortField = new JTextField();
        serverPortField.setBounds(130, 60, 170, 30);

        JButton connectButton = new JButton("Connect with Server");
        connectButton.setBounds(70, 110, 200, 30);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverPortText = serverPortField.getText();

                int serverPort = Integer.parseInt(serverPortText);
                if (serverPort==8080)
                connectToServer();
                if (serverPort==8090)
                    connectToCA();
            }
        });

        frame.add(serverIPLabel);
        frame.add(serverIPField);
        frame.add(serverPortLabel);
        frame.add(serverPortField);
        frame.add(connectButton);

        frame.setVisible(true);
    }
}

